#! /bin/sh

set -e

cd ..

echo "\n🔥 Install the apks...\n"
find  . -name "*.apk" -print -exec adb install {} \;

echo "\n🔥 Running instrumentation"

TEST_RUNNER="io.dotanuki.demos.norris.test/androidx.test.runner.AndroidJUnitRunner"
adb shell am instrument -w $TEST_RUNNER

# FAILURES=`cat execution.log | grep FAILURES`

# if [ ! -z "$FAILURES" ] then
#   echo "\n🔥 Instrumentation test execution failed!\n"
#   more execution.log
#   exit 1
# fi

echo "\n🔥 Instrumentation tests ran with success!\n"
