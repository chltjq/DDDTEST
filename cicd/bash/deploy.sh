#!/bin/sh
# This script is called by the build pipeline. It deploys the CloudFormation stack and copies the app files to S3.
# There is one parameter required for this script:
#   environment: the simple name of the environment to which we'll deploy; e.g. "dev" or "stg" or "prod"
#
# The following environment variables will be used from the vars.sh script:
#   appName: the name of the npm package pulled from package.json
#   branch: the abbreviated, formatted branch name based on the story number, e.g. "abc1234" or "master"
#   ARTIFACTS_BUCKET: the name of the AWS S3 bucket to be used for storing build artifacts
#   s3Folder: the path to be used in the S3 bucket for storing artifacts, recommended to be structured something like this:
#              ${appName}/${branch}/${releaseVersion}
#
# In addition, "websiteBucketName" will be extracted from the output variable "outBucketName" of the deployed CloudFormation stack

# Ensures script execution halts on first error
set -exo pipefail

readonly environment=$1

source cicd/bash/vars.sh

# Testing to confirm that these variables are set.
[[ $environment ]]
[[ $appName ]]
[[ $branch ]]
[[ $ARTIFACTS_BUCKET ]]
[[ $s3Folder ]]

readonly waitTime='15s'
stackName=${environment}-${appName}

featureBranchName=
if [[ ${branch} != "develop" ]] && [[ ${branch} != "master" ]]; then
    # Feature branches need a special indicator in the stack name
    stackName=${environment}-${appName}-feature-${branch}
    featureBranchName=${branch}
fi

getStackStatus(){
    # Query for CFN stacks, filtering out the stacks that are in DELETE_COMPLETE status
    # This makes the query run significantly faster
    readonly filter="CREATE_IN_PROGRESS CREATE_FAILED CREATE_COMPLETE ROLLBACK_IN_PROGRESS ROLLBACK_FAILED ROLLBACK_COMPLETE DELETE_IN_PROGRESS DELETE_FAILED UPDATE_IN_PROGRESS UPDATE_COMPLETE_CLEANUP_IN_PROGRESS UPDATE_COMPLETE UPDATE_ROLLBACK_IN_PROGRESS UPDATE_ROLLBACK_FAILED UPDATE_ROLLBACK_COMPLETE_CLEANUP_IN_PROGRESS UPDATE_ROLLBACK_COMPLETE REVIEW_IN_PROGRESS"
    readonly query="StackSummaries[?StackName=='$stackName'].StackStatus"
    stackStatus=$(aws cloudformation list-stacks --stack-status-filter $filter --query $query --output text)
    
    echo "CloudFormation stack $stackName, current status: $stackStatus (non-existent if blank)"
}

# Get the status of the CFN stack that will be deployed
getStackStatus


cfParameters="paramAppName=${appName} paramEnvironment=${environment} paramBranch=${featureBranchName} paramDomainName=${domainName} paramHostedZone=${hostedZone} paramAcmCertificateArn=${acmCertification} paramLoggingBucketID=${LOGGING_BUCKET}"

if [[ ${environment} == "prod" ]]
then
    aws cloudformation deploy \
    --template-file cicd/cfn/serverless-web.cft.yml \
    --stack-name ${stackName} \
    --no-fail-on-empty-changeset \
    --role-arn ${role_arn} \
    --tags ${commonTags} \
    --parameter-overrides ${cfParameters}
else
    aws cloudformation deploy \
    --template-file cicd/cfn/serverless-web.cft.yml \
    --stack-name ${stackName} \
    --no-fail-on-empty-changeset \
    --tags ${commonTags} \
    --parameter-overrides ${cfParameters}
fi

# Wait until the stack status is no longer IN_PROGRESS
while [[ $stackStatus = *"IN_PROGRESS"* ]]
do
    echo "Sleeping for $waitTime to allow the stack progress to complete."
    sleep $waitTime
    getStackStatus
done

# export this so that the npm run update:url command can use the stack name to retrieve the url for testing the s3 bucket website
export STACK_NAME=$stackName

# update:url must be ran from here or else the STACK_NAME variable is no longer set after the completion of this shell script.
npm run update:url

if [[ ${branch} == "develop" ]] || [[ ${branch} == "master" ]]; then
    # non-feature branches get termination protection enabled
    aws cloudformation update-termination-protection --enable-termination-protection --stack-name $stackName
fi

# Get the website bucket name
websiteBucketName=$(aws cloudformation describe-stacks \
    --stack $stackName \
    --output text \
| awk '/outBucketName/ {print $NF}')

aws s3 sync s3://${ARTIFACTS_BUCKET}/${s3Folder}/app s3://${websiteBucketName} --no-progress --delete | awk 'NR<=10'
