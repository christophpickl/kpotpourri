#!/bin/bash

BASEDIR=`pwd`
COMMON_BUILD_LIBS="${BASEDIR}/common/build/libs/"
JAVADOC_ZIP=${COMMON_BUILD_LIBS}/common-1.0-SNAPSHOT-javadoc.zip
JAVADOC_TARGET_DIR=${COMMON_BUILD_LIBS}/javadoc

./gradlew clean build

mv ${COMMON_BUILD_LIBS}/common-1.0-SNAPSHOT-javadoc.jar ${JAVADOC_ZIP}
rm -rf ${JAVADOC_TARGET_DIR}
unzip ${JAVADOC_ZIP} -d ${JAVADOC_TARGET_DIR}
open ${JAVADOC_TARGET_DIR}
