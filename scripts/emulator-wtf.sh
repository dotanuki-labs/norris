#! /usr/bin/env bash

set -ex

dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "${dir%/*}"

readonly features=("facts" "search")
readonly mode="${1:-no-quiet}"

usage() {
    echo
    echo "Usage"
    echo
    echo "./emulatorwtf.sh"
    echo "./emulatorwtf.sh no-quiet (default)"
    echo "./emulatorwtf.sh quiet"
    echo
}

locate_product_apk() {

    local apk_file

    if [[ -z "$CI" ]]; then
        apk_file="app/build/outputs/apk/debug/app-debug.apk"
    else
        apk_file="debug-apk/app-debug.apk"
    fi

    if ! test -f "$apk_file"; then
        echo "𝙓 Error : $apk_file not found"
        echo
        return 1
    fi

    echo "$apk_file"
}

locate_instrumentation_tests_apk() {
    local feature="$1"
    local common_path="/build/outputs/apk/androidTest/debug/$feature-debug-androidTest.apk"
    local apk_file

    if [[ -z "$CI" ]]; then
        apk_file="features/$common_path/$feature"
    else
        apk_file="test-apks/features/$common_path/$feature"
    fi

    if ! test -f "$apk_file"; then
        echo "𝙓 Error : $apk_file not found"
        echo
        exit 1
    fi

    echo "$apk_file"

}

locate_reports_folder() {
    local feature="$1"
    local folder

    if [[ -z "$CI" ]]; then
        folder="build/reports/emulator-wtf/$feature"
    else
        folder="emulator-wtf-results/$feature"
    fi

    mkdir -p "$folder"
    echo "$folder"
}

require_ewcli() {
    if ! which ew-cli >/dev/null; then
        echo "𝙓 Error : 'ew-cli' binary not available"
        echo
        exit 1
    fi
}

require_emulatorwtf_token() {
    if [[ -z "$EW_API_TOKEN" ]]; then
        echo "𝙓 Error : expecting environment variable \$EW_API_TOKEN"
        echo
        exit 1
    fi
}

execute_tests_for_feature() {
    local feature="$1"
    local device_model="model=Pixel2,version=31"
    local apk
    local instrumentation
    local reports_folder

    apk=$(locate_product_apk)
    instrumentation=$(locate_instrumentation_tests_apk "$feature")
    reports_folder=$(locate_reports_folder "$feature")

    echo
    echo "• Running tests for : '$feature'"

    ew-cli --app "$apk" \
        --test "$instrumentation" \
        --outputs-dir "$reports_folder" \
        --device "$device_model" \
        --clear-package-data --use-orchestrator "--$mode"
}

execute_tests() {
    for feature in "${features[@]}"; do
        execute_tests_for_feature "$feature"
    done
}

require_ewcli
require_emulatorwtf_token
execute_tests

echo
echo "🔥 Done!"
echo
