#! /usr/bin/env bash

set -e

dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "${dir%/*}"

readonly cyan="\033[1;36m"
readonly normal="\033[0m"

run_ktlint() {
    if ! which ktlint >/dev/null; then
        echo -e "Error : ${cyan}ktlint${normal} required but not available"
        exit 1
    fi

    echo "• Checking code formatting"
    echo

    ktlint --reporter=sarif?group_by_file,output=reports/ktlint.sarif --relative || true
}

run_detekt() {
    if ! which detekt >/dev/null; then
        echo -e "Error : ${cyan}detekt${normal} required but not available"
        exit 1
    fi

    echo -e "${cyan}• Checking code smells"
    detekt -c .config/detekt.yml --build-upon-default-config --report sarif:reports/detekt.sarif || true
}

readonly help_ktlint="Run ktlint static analyser for this project"
readonly help_detekt="Run detekt static analyser for this project"
readonly help_all="Run ktlint and detekt for this project"

usage() {
    echo "Usage instructions:"
    echo
    echo -e "‣ code-style-kotlin.sh ${cyan}ktlint${normal}  $help_ktlint"
    echo -e "‣ code-style-kotlin.sh ${cyan}detekt${normal}  $help_detekt"
    echo -e "‣ code-style-kotlin.sh ${cyan}all${normal}     $help_all"
}

readonly what="$1"

main() {
    if [[ -z "$what" ]]; then
        echo "Error: no linter supplied"
        usage
        return
    fi

    rm -rf reports && mkdir reports

    case "$what" in
    "ktlint")
        run_ktlint
        ;;
    "detekt")
        run_detekt
        ;;
    "all")
        run_ktlint
        echo
        run_detekt
        ;;
    *)
        echo -e "Error: Invalid linter → ${cyan}$what${normal}"
        usage
        echo
        exit 1
        ;;
    esac
}

echo
main
echo
