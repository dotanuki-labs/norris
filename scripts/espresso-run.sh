#! /bin/sh

set -e

cd ..

echo "🔥 Install the apks...\n"
find  . -name "*.apk" -print -exec adb install {} \;

echo "\n🔥 Running instrumentation"
RUNNER="io.dotanuki.demos.norris.test/androidx.test.runner.AndroidJUnitRunner"
adb shell am instrument -w -r $RUNNER > adb-test.log

if [ $? -eq 0 ]; then
    echo "\n🔥 Instrumentation test execution failed! Aborting\n"
    exit 1
fi

cat adb-test.log | grep "INSTRUMENTATION_STATUS: stack=" | grep -v "org.junit.AssumptionViolatedException"
if [ $? -eq 0 ]; then
  echo "Test failure found"
  exit 1
fi

echo "\n🔥 Instrumentation tests ran with success!\n"
exit 0