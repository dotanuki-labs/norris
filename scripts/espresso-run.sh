#! /usr/bin/env bash

set -eu

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )";
cd "${DIR%/*}"

readonly JUNIT_RUNNER="io.dotanuki.demos.norris.test/androidx.test.runner.AndroidJUnitRunner"

echo
echo "🔥 Install apks ..."
find  . -name "*.apk" -print -exec adb install {} \; > /dev/null 2>&1

echo "🔥 Running instrumentation tests ..."
EXECUTION=$(adb shell am instrument -w "$JUNIT_RUNNER")
ERRORS_FOUND=`echo $EXECUTION | grep FAILURES | tr -d ' '`

echo -e "$EXECUTION"

if [ -n "$ERRORS_FOUND" ]; then
  echo
	echo "😞 Instrumentation test execution failed!"
	echo
	exit 1
fi

echo
echo "😎 Instrumentation tests ran with success!"
echo

exit 0
