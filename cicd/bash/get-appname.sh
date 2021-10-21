# appName is extracted from the package.json file, or the cicd/output/app-name.txt file, which get created during the build step
if [ -z "$appName" ]; then
  if [ -e cicd/output/app-name.txt ]
    then
      appName=$(cat cicd/output/app-name.txt) # app-name.txt was created by a previous build step
    else
      appName=$(node -pe "require('./package.json').name")
  fi
fi
echo $appName