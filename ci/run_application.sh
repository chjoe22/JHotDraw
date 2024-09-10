#!/bin/bash

set -e

CURRENT_FOLDER=$(basename "$PWD")

if [ "$CURRENT_FOLDER" = "ci" ]; then
    cd ..
fi

mvn clean install
