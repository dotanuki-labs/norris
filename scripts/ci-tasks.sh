#! /usr/bin/env bash

set -e

setup() {
    dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    cd "${dir%/*}"
}

readonly cyan="\033[1;36m"
readonly normal="\e[0m"

readonly help_linters="Runs Ktlint and Detekt for all configured modules"
readonly help_unit_tests="Runs JVM/Robolectric tests for all modules"
readonly help_screenshot_tests="Runs screenshot tests for all features. Requires an online device"
readonly help_acceptance_tests="Runs acceptance tests over the product. Requires an online device"

usage() {
    echo "Usage instructions:"
    echo
    echo -e " ‣ ci-tasks.sh ${cyan}linters${normal}              $help_linters"
    echo -e " ‣ ci-tasks.sh ${cyan}unit-tests${normal}           $help_unit_tests"
    echo -e " ‣ ci-tasks.sh ${cyan}screenshot-tests${normal}     $help_screenshot_tests"
    echo -e " ‣ ci-tasks.sh ${cyan}acceptance-tests${normal}     $help_acceptance_tests"
}

check_online_device() {
    DEVICES_COUNT=$(adb devices | grep -wc "device")

    if [[ $DEVICES_COUNT -gt "1" ]]; then
        echo "Error : You must have one(1) device online to run Instrumentation tests"
        echo "Aborting"
        exit 1
    fi
}

clean() {
    ./gradlew clean --no-daemon
}

static_analysis() {
    clean
    ./gradlew ktlintCheck detekt --no-daemon --stacktrace
}

jvm_tests() {
    clean
    ./gradlew test --no-daemon --stacktrace
}

clean_up_packages() {
    packages=$(adb shell pm list packages | grep norris || true)

    if [[ -z "$packages" ]]; then
        echo "No packages to clean"
        echo
        return
    fi

    echo "Cleaning up packages ..."
    adb uninstall io.dotanuki.demos.norris >/dev/null 2>&1 || true
    adb uninstall io.dotanuki.demos.norris.test >/dev/null 2>&1 || true
    adb uninstall io.dotanuki.demos.norris.debug.test >/dev/null 2>&1 || true
    echo
}

screenshot_tests() {
    check_online_device
    clean_up_packages
    clean
    ./screenshots-sync.sh pixel
    ./gradlew executeScreenTests --no-daemon --stacktrace
}

acceptance_tests() {
    check_online_device
    clean_up_packages
    clean
    ./gradlew app:assembleRelease app:assembleAndroidTest -PtestMode=true --no-daemon --stacktrace
    ./scripts/espresso-run.sh
}

readonly command="$1"

main() {
    if [[ -z "$command" ]]; then
        echo "Error: no command supplied"
        usage
        return
    fi

    case "$command" in
    "linters")
        static_analysis
        ;;
    "unit-tests")
        jvm_tests
        ;;
    "screenshot-tests")
        screenshot_tests
        ;;
    "acceptance-tests")
        acceptance_tests
        ;;
    *)
        echo -e "Error: Invalid command → ${cyan}$command${normal}"
        usage
        echo
        exit 1
        ;;
    esac
}

echo
setup
main
echo
