#! /usr/bin/env bash

set -e

setup() {
    dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    cd "${dir%/*}"
}

readonly cyan_color="\033[1;36m"
readonly red_color="\033[1;91m"
readonly normal_color="\033[0m"

readonly features=(
    "search"
    "facts"
)

readonly device="$1"

usage() {
    echo
    echo -e "Usage : ‣ screenshots-sync.sh $cyan_color<device>$normal_color (nexus4 or pixel)"
}

sync() {

    echo -e "Syncing screenshots for device profile : ${cyan_color}$device${normal_color}"
    echo

    for feature in "${features[@]}"
    do
        origin="features/$feature/screenshots/$device"
        destination="features/$feature/screenshots/debug"

        rm -rf $destination
        echo -e "✔︎ Cleaned contents at ${cyan_color}$destination${normal_color}"

        cp -R $origin $destination
        echo -e "✔︎ Copied images from ${cyan_color}$origin${normal_color} to ${cyan_color}$destination${normal_color}"

        echo
    done
}

main() {
    if [[ -z "$device" ]]; then
        echo -e "${red_color}Error: no device device supplied${normal_color}"
        usage
        return
    fi

    case "$device" in
    "nexus4"|"pixel")
        sync
        ;;
    *)
        echo -e "Error: Invalid device → ${red_color}$device${normal_color}"
        usage
        echo
        exit 1
        ;;
    esac
}

echo
setup
main
echo
