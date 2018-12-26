#!/usr/bin/env bash

LAST_RESULT=""
safeEval() {
    COMMAND=$1
    echo ""
    echo ">> $COMMAND"
    LAST_RESULT=`eval ${COMMAND}`
    if [[ $? -ne 0 ]] ; then
        echo "Last command did not end successful!" >&2
        osascript -e 'display notification "Executing command failed 😢" with title "KPotPourri Build"'
        exit 1
    fi
}
