#! /usr/bin/env bash

set -eu

readonly ewtf_home="$GITHUB_WORKSPACE/bin"
readonly ewtf="$ewtf_home/ew-cli"
readonly tests_apk="$GITHUB_WORKSPACE/tests-apk/app-release-androidTest.apk"
readonly target_apk="$GITHUB_WORKSPACE/release-apk/app-release.apk"

readonly api_token="$1"
readonly device_name="$2"

# Setup path

dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "${dir%/*}"

# Install Emulator WTF

mkdir -p "$ewtf_home"
curl -s --fail https://maven.emulator.wtf/releases/ew-cli -o "$ewtf" && chmod +x "$ewtf"


# Run tests

sh "$ewtf" \
    --token "$api_token" \
    --app "$target_apk" \
    --test "$tests_apk" \
    --device "device=$device_name,version=30,atd=true" \
    --outputs-dir emulator-wtf-results \
    --use-orchestrator \
    --clear-package-data
