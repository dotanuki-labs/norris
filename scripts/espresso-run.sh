#! /bin/bash

set -ex

cd ..

echo "\n🔥 Install the apks...\n"
find  . -name "*.apk" -print -exec adb install {} \;

echo "\n🔥 Running instrumentation"

EXECUTION=`adb shell am instrument -w io.dotanuki.demos.norris.test/androidx.test.runner.AndroidJUnitRunner`

ERRORS_FOUND=`echo $EXECUTION | grep FAILURES`

if [[ -z "$ERRORS_FOUND" ]]; then
  echo "\n🔥 Instrumentation tests ran with success!\n"
  exit 0
fi

echo "\n🔥 Instrumentation test execution failed!\n"
echo $EXECUTION
exit 1
