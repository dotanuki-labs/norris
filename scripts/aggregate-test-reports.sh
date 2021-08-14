#!/usr/bin/env bash

set -e

readonly REPORTS_FOLDER="$1"

if [[ -z "$REPORTS_FOLDER" ]]; then
	echo
	echo "Reports folder should be provided."
	echo "Usage example : ./aggregate-test-reports.sh build/test-reports"
	echo
	exit 1
fi

readonly JUNIT_XMLS="$REPORTS_FOLDER/junit"
readonly REPORT_FILE="$REPORTS_FOLDER/aggregated-report"
readonly REPORT_TITLE="Norris Test Suite"
readonly REPORT_LOGO="https://avatars.githubusercontent.com/u/47199894?s=50&v=4"

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "${DIR%/*}"

echo
echo "🔥 Setting up..."
rm -rf "$REPORTS_FOLDER"
mkdir -p "$REPORTS_FOLDER"
mkdir -p "$JUNIT_XMLS"

echo "🔥 Collecting test files"
find . -type f -regex ".*/build/test-results/.*xml" -exec cp {} "$JUNIT_XMLS/" \;

if command -v xunit-viewer >/dev/null 2>&1; then
	echo "🔥 Generating aggregated report with xunit-viewer"
	echo
	xunit-viewer -r "$JUNIT_XMLS" -o "$REPORT_FILE" -t "$REPORT_TITLE" -b "$REPORT_LOGO" -f "$REPORT_LOGO"
else
	echo "🔥 xunit-viewer not detected. Skipping custom report generation"
fi

echo
echo "🔥 Done!"
echo
