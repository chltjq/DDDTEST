#!/bin/sh
# This script is sourced by other shell scripts. It defines reusable variables across multiple steps of the pipeline.
# The variables defined by this script are:
#   branch: the abbreviated, formatted branch name based on the story number, e.g. "abc1234" or "master"
#   appName: the name of the npm package pulled from package.json
#   releaseVersion: the unique build version, including feature branch pre-release indicator, e.g. "0.1.123-abc456"
#   s3Folder: the path to be used in the S3 bucket for storing artifacts, recommended to be structured something like this:
#              ${appName}/${branch}/${releaseVersion}

branch=$(sh ./cicd/bash/get-current-branch.sh)

# appName is extracted from the package.json file, or the cicd/output/app-name.txt file, which are created during the build step
appName=$(sh ./cicd/bash/get-appname.sh)

# releaseVersion gets computed and then saved to the cicd/output/release-version.txt file during the build step
if [ -z "$releaseVersion" ]; then # -z option means if string is null or zero length
  if [ -e cicd/output/release-version.txt ] # -e option means if the target exists
    then
      releaseVersion=$(cat cicd/output/release-version.txt) # release-version.txt was created by a previous build step in build.sh
    else
      releaseVersion=$(sh cicd/bash/get-release-version.sh $branch)
  fi
fi

s3Folder=${appName}/${branch}/${releaseVersion}