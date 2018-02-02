#!/usr/bin/env bash

TARGET=build/__dependencies__.txt
echo "Generating dependency trees for all submodules and saving it to: [$TARGET]"
./gradlew listDependencies > ${TARGET}
open ${TARGET}
