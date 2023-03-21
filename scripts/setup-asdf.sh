#! /usr/bin/env bash

set -e

dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "${dir%/*}"

readonly cyan="\033[1;36m"
readonly normal="\033[0m"

echo "Setting up local CLI tooling"

if ! which asdf >/dev/null; then
    echo -e "Error : ${cyan}asdf${normal} required but not available"
    echo
    exit 1
fi

echo
asdf plugin add ktlint
asdf plugin add detekt https://github.com/dotanuki-labs/asdf-detekt.git
asdf plugin add maestro https://github.com/dotanuki-labs/asdf-maestro.git
asdf install
echo

echo "Done!"
echo
