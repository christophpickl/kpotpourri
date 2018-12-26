#!/usr/bin/env bash


source bin/includes.sh

safeEval "./gradlew clean test"

SUCESS_MESSAGE="Test build finished successfully ✅"
echo ${SUCESS_MESSAGE}
osascript -e "display notification \"${SUCESS_MESSAGE}\" with title \"KPotPourri Test\""
