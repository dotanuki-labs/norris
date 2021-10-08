#! /usr/bin/env bash

set -e

setup() {
    dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    cd "${dir%/*}"
}

readonly cyan="\033[1;36m"
readonly red="\033[1;91m"
readonly normal="\e[0m"

readonly features=(
    "search"
    "facts"
)

readonly device="$1"

usage() {
    echo
    echo -e "Usage : ‣ screenshots-sync.sh ${cyan}$device${normal} (nexus4 or pixel)"
}

sync() {

    echo -e "Syncing screenshots for device profile : ${cyan}$device${normal}"
    echo

    for feature in "${features[@]}"
    do
        origin="features/$feature/screenshots/$device"
        destination="features/$feature/screenshots/debug"

        rm -rf $destination
        echo -e "✔︎ Cleaned contents at ${cyan}$destination${normal}"

        cp -R $origin $destination
        echo -e "✔︎ Copied images from ${cyan}$origin${normal} to ${cyan}$destination${normal}"

        echo
    done
}

main() {
    if [[ -z "$device" ]]; then
        echo -e "${red}Error: no device device supplied${normal}"
        usage
        return
    fi

    case "$device" in
    "nexus4"|"pixel")
        sync
        ;;
    *)
        echo -e "${red}Error: Invalid device${normal} → ${cyan}$device${normal}"
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
