#!/usr/bin/env bash

echo "./gradlew clean test"

./gradlew clean test
RESULT_CODE=$?

if [[ ${RESULT_CODE} -eq 0 ]]; then
    echo "[KPOT] tests SUCCESS."
else
    echo "[KPOT] tests FAILED."
fi
