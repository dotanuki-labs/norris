#!/bin/bash

set -e

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )";
cd "${DIR%/*}"

./gradlew clean --no-daemon
./gradlew ktlintCheck detekt --no-daemon --stacktrace
./gradlew test --no-daemon --stacktrace
./gradlew app:connectedAndroidTest --no-daemon --stacktrace -PtestMode=true
