#!/usr/bin/env bash
# This script is called by the build pipeline. It builds the app and runs unit tests
# There is one parameter required for this script:
#   release_version: This is the version of the build to be output to the version.js file

# The output of this build script will produce a file named "version.js" in the src directory
# The version.js file can be imported by app files to pull in the appLastUpdated (build date) and version

# Ensures script execution halts on first error
set -exo pipefail

readonly release_version=$1
readonly environment=$2

# Testing to confirm that these variables are set.
[[ $release_version ]]
[[ $environment ]]

# Install Dependencies - Removed to use yarn
# npm ci --loglevel warn

# Lint
# npm run lint
# TODO: Find out why there is not linting

# Write out the build date and build version to be baked into the build
today=$(date +%m/%d/%Y)
versionJsFile=src/version.js

echo "export const appLastUpdated = \"${today}\";" > $versionJsFile
echo "export const version = \"${release_version}\";" >> $versionJsFile

# Unit Tests
#### The following command has been tested only with Angular 6
#### It requires the karma config to include a chromeHeadless custom launcher, like this:
#    customLaunchers: {
#      chromeHeadless: {
#        base: 'ChromeHeadless',
#      }
#    },

# npm run test:unit:coverage #-- --browsers=chromeHeadless --code-coverage=true --watch=false

rm -rf dist

# Build the app
yarn run build:$environment

if [[ ${LOCAL_PUBLISH_DIR} != "dist" ]]; then
  # copy the built app to the dist directory, to be stored as a build artifact
  cp -R ${LOCAL_PUBLISH_DIR} dist
fi

# Save the app name and release version as part of the build output
mkdir -p cicd/output
node -pe "require('./package.json').name" > cicd/output/app-name.txt
echo $release_version > cicd/output/release-version.txt
