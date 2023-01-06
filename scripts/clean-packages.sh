#!/usr/bin/env bash

set -e

readonly packages="io.dotanuki.norris"

echo
echo "Uninstalling packages for $packages"

adb shell pm list packages $packages | cut -d ':' -f 2 | tr -d '\r' | xargs -L1 -t adb uninstall >/dev/null

echo
echo "✅ Done"
echo
