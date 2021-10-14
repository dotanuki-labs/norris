#! /usr/bin/env bash

set -eu

dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "${dir%/*}"

readonly junit_runner="io.dotanuki.demos.norris.test/androidx.test.runner.AndroidJUnitRunner"

echo
echo "🔥 Install apks ..."
find . -name "*.apk" -print -exec adb install {} \; >/dev/null 2>&1

echo "🔥 Running instrumentation tests ..."
execution=$(adb shell am instrument -w "$junit_runner")
errors_found=$(echo "$execution" | grep "FAILURES" | tr -d ' ')

echo -e "$execution"

if [ -n "$errors_found" ]; then
    echo
    echo "😞 Instrumentation test execution failed!"
    echo
    exit 1
fi

echo
echo "😎 Instrumentation tests ran with success!"
echo

exit 0
