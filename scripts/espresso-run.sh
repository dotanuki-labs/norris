#! /bin/bash

set -eu

cd ..

echo
echo "🔥 Install the apks.."

find  . -name "*.apk" -print -exec adb install {} \;

echo
echo "🔥 Running instrumentation"

EXECUTION=`adb shell am instrument -w io.dotanuki.demos.norris.test/androidx.test.runner.AndroidJUnitRunner`

ERRORS_FOUND=`echo $EXECUTION | grep FAILURES | tr -d ' '`

if [ -n "$ERRORS_FOUND" ]; then
  echo
	echo "🔥 Instrumentation test execution failed!"
	echo
	echo -e $EXECUTION
	exit 1
fi

echo
echo "🔥 Instrumentation tests ran with success!"
echo

exit 0
