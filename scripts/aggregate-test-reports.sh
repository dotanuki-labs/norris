#!/usr/bin/env bash

set -e

readonly reports_folder="$1"

if [[ -z "$reports_folder" ]]; then
    echo
    echo "Reports folder should be provided."
    echo "Usage example : ./aggregate-test-reports.sh build/test-reports"
    echo
    exit 1
fi

readonly junit_xmls="$reports_folder/junit"
readonly report_file="$reports_folder/aggregated-report"
readonly report_title="Norris Test Suite"
readonly report_logo="https://avatars.githubusercontent.com/u/47199894?s=50&v=4"

dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "${dir%/*}"

echo
echo "🔥 Setting up..."
rm -rf "$reports_folder"
mkdir -p "$reports_folder"
mkdir -p "$junit_xmls"

echo "🔥 Collecting test files"
find . -type f -regex ".*/build/test-results/.*xml" -exec cp {} "$junit_xmls/" \;

if command -v xunit-viewer >/dev/null 2>&1; then
    echo "🔥 Generating aggregated report with xunit-viewer"
    echo
    xunit-viewer -r "$junit_xmls" -o "$report_file" -t "$report_title" -b "$report_logo" -f "$report_logo"
else
    echo "🔥 xunit-viewer not detected. Skipping custom report generation"
fi

echo
echo "🔥 Done!"
echo
