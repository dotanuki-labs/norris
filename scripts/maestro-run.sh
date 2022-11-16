#! /usr/bin/env bash

set -e

dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "${dir%/*}"

readonly maestro_bin="$HOME/.maestro/bin/maestro"
readonly maestro_workspace=".maestro"
readonly mobile_dev_cloud_token="$1"
readonly apk_file="$2"

current_branch_name=


usage() {
    echo
    echo "Usage"
    echo
    echo "./maestro-run.sh <mobile-dev-api-token> <path-to-apk-file>"
    echo
}

require_arguments() {
    if [[ $# != 2 ]]; then
        usage
        exit 1
    fi
}

resolve_maestro_binary() {
    if test -f "$maestro_bin"; then
        echo "‣ Found maestro installation at user : $maestro_bin"
    else
        echo "‣ maestro installation not found. Installing latest version ..."
        echo
        curl -Ls "https://get.maestro.mobile.dev" | bash
        echo
    fi
}

execute_tests() {
    if [[ -z "$GITHUB_ACTIONS" ]]; then
        local branch="$(git rev-parse --abbrev-ref HEAD)"
    else
        if [[ -z "$GITHUB_HEAD_REF" ]]; then
            local branch="master"
        else
            local branch="$GITHUB_HEAD_REF"
        fi
    fi

    local commit="$(git rev-parse --short HEAD)"
    execution_name="norris-$branch@$commit"

    echo "‣ Execution name will be : $execution_name"
    echo "‣ Running tests from workspace : $maestro_workspace"
    echo
    "$maestro_bin" cloud --apiKey "$mobile_dev_cloud_token" "$apk_file" --name "$execution_name" "$maestro_workspace"
}

echo
require_arguments "$@"
resolve_maestro_binary
execute_tests
echo
