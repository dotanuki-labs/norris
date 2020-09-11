#!/bin/sh

set -ex

./gradlew clean --no-daemon
./gradlew ktlintCheck --no-daemon --stacktrace
./gradlew detekt --no-daemon --stacktrace
./gradlew jacocoTestReport jacocoTestReportDebug --no-daemon --stacktrace
./gradlew app:assembleDebug --no-daemon --stacktrace
./gradlew app:connectedAndroidTest --no-daemon --stacktrace -PtestMode=true