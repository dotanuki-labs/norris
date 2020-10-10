#! /bin/sh

set -e

cd ..

echo "\n🔥 Install the apks...\n"
find  . -name "*.apk" -print -exec adb install {} \;

echo "\n🔥 Running instrumentation"

adb logcat &
adb shell 'am instrument -w io.dotanuki.demos.norris.test/androidx.test.runner.AndroidJUnitRunner; echo $?' | grep 'FAILURES'

if [ $? -eq 0 ;] then
  echo "\n🔥 Instrumentation test execution failed! Aborting\n"
  exit 1
fi

echo "\n🔥 Instrumentation tests ran with success!\n"
