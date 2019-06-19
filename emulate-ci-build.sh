#!/bin/sh

./gradlew --stop
./gradlew clean test assembleDebug -xlint --stacktrace