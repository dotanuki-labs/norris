#! /bin/bash

set -eu

readonly JUNIT_RUNNER="io.dotanuki.demos.norris.test/androidx.test.runner.AndroidJUnitRunner"

cd ..

echo
echo "🔥 Install the apks.."

find  . -name "*.apk" -print -exec adb install {} \;

echo
echo "🔥 Running instrumentation"

EXECUTION=$(adb shell am instrument -w "$JUNIT_RUNNER")

echo -e $EXECUTION

ERRORS_FOUND=`echo $EXECUTION | grep FAILURES | tr -d ' '`

if [ -n "$ERRORS_FOUND" ]; then
  echo
	echo "🔥 Instrumentation test execution failed!"
	echo
	exit 1
fi

echo
echo "🔥 Instrumentation tests ran with success!"
echo

exit 0
