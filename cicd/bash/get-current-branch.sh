#!/bin/sh
# This script is executed by other shell scripts. It detects the git branch name from the environment and
# transforms it into a shortened label for the build and deployment process.
#
# This script has been tested with Bitbucket Pipelines and AWS CodeBuild

# The value returned by this script is:
#   branch: the abbreviated, formatted branch name based on the story number, e.g. "abc1234" or "master"

# Format the branch name
if [ -z "$branch" ]; then
  branch=${BRANCH}
  # Format the branch from "refs/heads/feature/abc-123_my_branch" to "abc123"

  if [  $(echo $BRANCH | cut -d\/ -f2) = "pull" ]; then
    branch="pr-"$(echo $branch | cut -d\/ -f3)
  fi

  if [ $(echo $BRANCH | cut -d\/ -f3) = "develop" ] || [ $(echo $BRANCH | cut -d\/ -f3) = "main" ]; then  # [ $branch = "refs/heads/develop" ] || [ $branch = "refs/heads/main" ]; then
    branch=$(echo $branch | cut -d\/ -f3 | cut -d\- -f1-2 | cut -d\_ -f1 | tr '[:upper:]' '[:lower:]' | sed 's/-//g ; s/_//g')
  else
    branch=$(echo $branch | cut -d\/ -f4 | cut -d\- -f1-2 | cut -d\_ -f1 | tr '[:upper:]' '[:lower:]' | sed 's/-//g ; s/_//g')
  fi

  # echo -e "\nbranch has been set to '$branch'\n" 1>&2
  # else
  # echo -e "\nbranch is already defined as '$branch'\n" 1>&2
fi

echo $branch
