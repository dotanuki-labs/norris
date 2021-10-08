#! /usr/bin/env bash

set -e

readonly cyan="\033[1;36m"
readonly red="\033[1;91m"
readonly normal="\e[0m"

readonly device="$1"

mapped_profile() {
    echo -e "Mapped emulator profile  -> ${cyan}$1${normal}"
    echo "::set-output name=assigned::$1"
}

usage() {
    echo
    echo -e "Usage : ‣ emulator-assigner.sh ${cyan}device${normal} (nexus4 or pixel)"
    exit 1
}

main() {
    if [[ -z "$device" ]]; then
        echo -e "${red}Error: no device device supplied${normal}"
        usage
    fi

    case "$device" in
    "pixel")
        mapped_profile "pixel"
        ;;
    "nexus4")
        mapped_profile "Nexus 4"
        ;;
    *)
        echo -e "${red}Invalid device name -> $device${normal}"
        usage
        ;;
    esac
}

echo
main
echo
