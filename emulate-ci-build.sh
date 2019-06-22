#!/bin/sh

set -ex

./gradlew --stop
./gradlew clean ktlintCheck --no-daemon --stacktrace
./gradlew test --no-daemon --stacktrace
./gradlew assembleDebug -xlint --no-daemon --stacktrace