#! /bin/sh

set -e

cd ..

echo "🔥 Install the apks...\n"
find  . -name "*.apk" -print -exec adb install {} \;

echo "\n🔥 Running instrumentation"
RUNNER="io.dotanuki.demos.norris.test/androidx.test.runner.AndroidJUnitRunner"
adb shell "am instrument -w $RUNNER; echo $?" | grep "FAILURES" # https://stackoverflow.com/a/58452689/1880882

if [ $? -eq 0 ]; then
    echo "Instrumentation test execution failed"
    exit 1
fi

exit 0