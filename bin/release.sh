#!/bin/bash

START=`date +%s`
CWD=`pwd`

VERSION_FILENAME=version.txt
VERSION_FILEPATH="$CWD/$VERSION_FILENAME"

myEcho() {
    echo "[RELEASE] $1"
}
checkLastCommand() {
    if [ $? -ne 0 ] ; then
        myEcho "Last command did not end successful!"
        exit 1
    fi
}
changeVersion() {
    echo
    myEcho "Changing version in [${VERSION_FILENAME}] to: $1"
    echo "$1" > ${VERSION_FILENAME}
    checkLastCommand
}

CURRENT_VERSION=`cat ${VERSION_FILEPATH}`

MAJOR_VERSION=$(echo ${CURRENT_VERSION}| cut -d "." -f 1)
CURRENT_MINOR_VERSION=$(echo ${CURRENT_VERSION}| cut -d "." -f 2)
NEXT_MINOR_VERSION=$(($CURRENT_MINOR_VERSION + 1))
NEXT_VERSION="$MAJOR_VERSION.$NEXT_MINOR_VERSION"
NEXT_VERSION_SYSPROPERTY="-Dkpotpourri.version=${NEXT_VERSION}"

myEcho "Release Summary:"
myEcho "------------------------------------"
myEcho "  Next version: $NEXT_VERSION"
myEcho "  System property: $NEXT_VERSION_SYSPROPERTY"


while true; do
    read -p "[RELEASE] Do you confirm this release? [y/n] >> " yn
    case ${yn} in
        [Yy]* ) break;;
        [Nn]* ) echo "Aborted"; exit;;
        * ) myEcho "Please answer y(es) or n(o)";;
    esac
done
echo

echo
myEcho "Updating GIT"
myEcho "------------------------------------"
git pull
checkLastCommand
git fetch -p
checkLastCommand
git push
checkLastCommand

echo
myEcho "Verifying release build"
myEcho "------------------------------------"
#./gradlew clean check checkTodo test ${NEXT_VERSION_SYSPROPERTY}
./gradlew clean check test ${NEXT_VERSION_SYSPROPERTY}
checkLastCommand

myEcho "Local changes"
myEcho "------------------------------------"
changeVersion ${NEXT_VERSION}
git add .
checkLastCommand
git commit -m "[Auto-Release] KPotpourri version: $NEXT_VERSION"
checkLastCommand
git tag ${NEXT_VERSION}
checkLastCommand

myEcho "Uploading to bintray"
myEcho "------------------------------------"
./gradlew bintrayUpload ${NEXT_VERSION_SYSPROPERTY}
checkLastCommand

myEcho "Pushing to GIT"
myEcho "------------------------------------"
git push
checkLastCommand
git push origin --tags
checkLastCommand



END=`date +%s`
ELAPSED=$(( $END - $START ))
echo
echo
myEcho "Release $NEXT_VERSION SUCCESSFULL"
myEcho "===================================="
echo
myEcho "Time needed: $ELAPSED seconds"
myEcho
echo

exit 0
