#! /bin/sh

set -e

cd ..

echo "\n🔥 Install the apks...\n"
find  . -name "*.apk" -print -exec adb install {} \;

echo "\n🔥 Running instrumentation"

EXECUTION=`adb shell am instrument -w io.dotanuki.demos.norris.test/androidx.test.runner.AndroidJUnitRunner`

echo $EXECUTION

FAILURES=`echo $EXECUTION | grep FAILURES`

if [ ! -z "$FAILURES" ] then
  echo "\n🔥 Instrumentation test execution failed!\n"
  more execution.log
  exit 1
fi

echo "\n🔥 Instrumentation tests ran with success!\n"
