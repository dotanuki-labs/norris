#! /usr/bin/env bash

set -euo pipefail

dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "${dir%/*}"

readonly maestro_workspace="functional-tests"
readonly apk_file="$1"

target_runner=

detect_runner() {
    while [[ $# -gt 0 ]]; do
        case "$1" in
        "cloud" | "local")
            target_runner="$1"
            shift 1
            ;;
        *)
            if [[ -z "$CI" ]]; then
                target_runner="local"
                local machine="Desktop"
            else
                target_runner="cloud"
                local machine="CI"
            fi
            echo "‣ No runner specified. Using '$target_runner' as default since $machine machine detected"
            shift 1
            ;;
        esac
    done
}

usage() {
    echo
    echo "Usage"
    echo
    echo "./maestro.sh path-to-apk-file (runner)"
    echo
    echo "Examples"
    echo
    echo "./maestro.sh app/build/outputs/apk/release/app-release.apk"
    echo "./maestro.sh app/build/outputs/apk/release/app-release.apk local"
    echo "./maestro.sh app/build/outputs/apk/release/app-release.apk cloud"
    echo
    echo "When 'runner' not specified, this script infer the best option according the environment (Desktop or CI)"
    echo
}

require_arguments() {
    if [[ $# == 0 ]]; then
        usage
        exit 1
    fi
}

install_apk_on_emulator() {
    if ! which adb >/dev/null; then
        echo "𝙓 Error : running maestro requires 'adb' installed and available in your \$PATH"
        echo
        exit 1
    fi

    echo "‣ Installing target apk on emulator"
    adb install "$apk_file" >/dev/null
}

require_maestro_binary() {
    if ! which maestro >/dev/null; then
        echo "𝙓 Error : 'maestro' required but not available"
        echo
        exit 1
    fi
}

require_maestro_token() {
    if [[ -z "$MOBILE_DEV_CLOUD_TOKEN" ]]; then
        echo "𝙓 Error : expecting environment variable \$MOBILE_DEV_CLOUD_TOKEN"
        echo
        exit 1
    fi
}

execute_tests_local() {
    install_apk_on_emulator
    echo "‣ Running tests from workspace : $maestro_workspace"
    echo
    maestro test "$maestro_workspace/acceptance.yaml"
}

execute_tests_cloud() {
    require_maestro_token
    local branch
    local commit

    if [[ -z "$GITHUB_ACTIONS" ]]; then
        branch="$(git rev-parse --abbrev-ref HEAD)"
    else
        if [[ -z "$GITHUB_HEAD_REF" ]]; then
            branch="master"
        else
            branch="$GITHUB_HEAD_REF"
        fi
    fi

    commit="$(git rev-parse --short HEAD)"
    execution_name="norris-$branch@$commit"

    echo "‣ Execution name will be : $execution_name"
    echo "‣ Running tests from workspace : $maestro_workspace"
    echo
    maestro cloud --apiKey "$MOBILE_DEV_CLOUD_TOKEN" "$apk_file" --name "$execution_name" "$maestro_workspace"
}

echo
require_arguments "$@"
detect_runner "$@"
require_maestro_binary

case "$target_runner" in
"local")
    execute_tests_local
    ;;
"cloud")
    execute_tests_cloud
    ;;
*)
    echo "𝙓 Error : invalid runner. Aborting"
    usage
    exit
    ;;
esac

echo
