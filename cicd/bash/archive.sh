#!/bin/sh
# This script is called by the build pipeline. It archives the build artifacts to S3
# The following environment variables will be used from the vars.sh script:
#   appName: the name of the npm package pulled from package.json
#   branch: the abbreviated, formatted branch name based on the story number, e.g. "abc1234" or "master"
#   s3Folder: the path to be used in the S3 bucket for storing artifacts
#              This is recommended to be structured something like this:
#                  ${appName}/${branch}/${releaseVersion}

# The following variable must be pre-defined by the build container:
#   ARTIFACTS_BUCKET: the name of the AWS S3 bucket to be used for storing build artifacts

# Ensures script execution halts on first error
set -exo pipefail

source cicd/bash/vars.sh

# Testing to confirm that these variables are set.
[[ $appName ]]
[[ $branch ]]
[[ $s3Folder ]]
[[ $ARTIFACTS_BUCKET ]]

# The Build step must copy the build output to the "dist" directory
# Archive the artifacts
aws s3 cp dist s3://${ARTIFACTS_BUCKET}/${s3Folder}/app --no-progress --recursive | awk 'NR<=10'
aws s3 cp cicd/output/release-version.txt s3://${ARTIFACTS_BUCKET}/${s3Folder}/app/version.html --no-progress
aws s3 cp cicd/cfn/serverless-web.cft.yml s3://${ARTIFACTS_BUCKET}/${s3Folder}/cfn/serverless-web.cft.yml --no-progress
if [[ ${branch} == "develop" ]] || [[ ${branch} == "master" ]]; then
    # master branch artifacts get synced to the "latest" artifact location
    timestamp=$(date "+%Y%m%d%H%M%S")
    aws s3 sync s3://${ARTIFACTS_BUCKET}/${s3Folder} s3://${ARTIFACTS_BUCKET}/${appName}/${branch}/backup/${timestamp} --no-progress --delete --exclude "backup/*" --exclude "latest/*"
fi
# if [[ ${branch} == "master" ]]; then
#   # master branch artifacts get synced to the "latest" artifact location
#   aws s3 sync s3://${ARTIFACTS_BUCKET}/${s3Folder} s3://${ARTIFACTS_BUCKET}/${appName}/master/latest --no-progress --delete | awk 'NR<=10'
# fi
