#! /usr/bin/env bash

set -e

dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "${dir%/*}"

readonly cyan="\033[1;36m"
readonly normal="\033[0m"

readonly cache_dir="$HOME/.dotanuki/cached"

run_ktlint() {

    if ! test -f "$HOME/.asdf/shims/ktlint"; then
        echo -e "Error : $(cyan "ktlint") not properly installed"
        echo "Setup your environtment with"
        echo -e "$> $(cyan "asdf plugin add ktlint")"
        echo -e "$> $(cyan "asdf install")"
        echo
        exit 1
    fi

    if ! which ktlint >/dev/null; then
        echo -e "Error : $(cyan "ktlint") required but not available"
        exit 1
    fi

    echo "• Checking code formatting"
    echo

    ktlint --reporter=plain?group_by_file --android || echo

    echo
    echo -e "• No issues found by${cyan}ktlint${normal}"
}

readonly detekt_version="1.22.0"
readonly detekt_home="$cache_dir/detekt"
readonly detekt_bin="$detekt_home/detekt-cli-$detekt_version/bin/detekt-cli"
readonly detekt_releases="https://github.com/detekt/detekt/releases/download"

run_detekt() {

    if ! test -f "$detekt_bin"; then
        echo -e "${cyan}• Installing detekt (v$detekt_version)${normal}"
        mkdir -p "$detekt_home"

        local detekt_zip="detekt-cli-$detekt_version.zip"

        rm -rf "${detekt_home:?}/${detekt_zip:?}"
        curl -sSL "$detekt_releases/v$detekt_version/$detekt_zip" -o "$cache_dir/$detekt_zip"
        unzip "$cache_dir/$detekt_zip" -d "$detekt_home" >/dev/null 2>&1
        chmod +x "$detekt_bin"
    fi

    echo -e "${cyan}• Running detekt (v$detekt_version)${normal}"
    "$detekt_bin" -c .config/detekt.yml --build-upon-default-config
    echo -e "${cyan}• No issues found with detekt${normal}"
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
