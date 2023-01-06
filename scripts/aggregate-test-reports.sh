#!/usr/bin/env bash

set -e

readonly reports_folder="$1"
readonly junit_xmls="$reports_folder/junit"

dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "${dir%/*}"

if [[ -z "$reports_folder" ]]; then
    echo
    echo "Reports folder should be provided."
    echo "Usage example : ./aggregate-test-reports.sh build/test-reports"
    echo
    exit 1
fi

echo
echo "🔥 Setting up..."
rm -rf "$reports_folder"
mkdir -p "$reports_folder"
mkdir -p "$junit_xmls"

echo "🔥 Collecting test files"
find . -type f -regex ".*/build/test-results/.*xml" -exec cp {} "$junit_xmls/" \;
find . -type f -regex ".*/build/outputs/androidTest-results/.*xml" -exec cp {} "$junit_xmls/" \;

echo
echo "🔥 Done!"
echo
