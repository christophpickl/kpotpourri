#!/usr/bin/env bash

echo "./gradlew clean test"

./gradlew clean test

RESULT_CODE=$?
echo "[KPOT] tests DONE."

# Samantha, Tessa, Victoria, Anna (DE)
if [ ${RESULT_CODE} -eq 0 ]; then
    say -v Anna "Die Tests wurden erfolgreich abgeschlossen."
else
    say -v Anna "Die Tests sind fehlgeschlagen."
fi
