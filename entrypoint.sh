if [[ ${GIT_REPO} ]]; then
  echo "Cloning custom repository ${GIT_REPO}"
  rm -rf code-directory
  git clone ${GIT_REPO} code-directory
  cd code-directory
fi 

if [[ ${VERSION} ]]; then
  echo "Updating parent version to ${VERSION}"
  mvn versions:update-parent -DparentVersion=[${VERSION}]
fi 

if [[ ${CUSTOM_COMMAND} ]]; then
  echo "Running custom Command ${CUSTOM_COMMAND}"
  ${CUSTOM_COMMAND}
else
  mvn clean test
fi 
