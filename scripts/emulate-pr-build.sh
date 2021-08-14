#! /usr/bin/env bash

set -e

setup() {
	DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
	cd "${DIR%/*}"
	./gradlew clean --no-daemon
}

static_analysis() {
	./gradlew ktlintCheck detekt --no-daemon --stacktrace
}

jvm_tests() {
	./gradlew test --no-daemon --stacktrace
}

check_online_device() {
	DEVICES_COUNT=$(adb devices | grep -wc "device")

	if [[ $DEVICES_COUNT -gt "1" ]]; then
		echo "Error : You must have one(1) device online to run Instrumentation tests"
		echo "Aborting"
		exit 1
	fi
}

screenshot_tests() {
	./gradlew executeScreenTests --no-daemon --stacktrace
}

acceptance_tests() {
	./gradlew app:assembleRelease app:assembleAndroidTest -PtestMode=true --no-daemon --stacktrace
	./scripts/espresso-run.sh
}

setup
check_online_device
static_analysis
jvm_tests
screenshot_tests
acceptance_tests
