#!/bin/sh

./gradlew --stop
./gradlew clean ktlintCheck test --stacktrace
./gradlew assembleDebug -xlint --stacktrace