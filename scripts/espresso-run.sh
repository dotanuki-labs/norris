#! /bin/sh

set -e

cd ..

echo "🔥 Install the apks...\n"
find  . -name "*.apk" -print -exec adb install {} \;

echo "\n🔥 Running instrumentation"
adb shell am instrument -w io.dotanuki.demos.norris.test/androidx.test.runner.AndroidJUnitRunner
