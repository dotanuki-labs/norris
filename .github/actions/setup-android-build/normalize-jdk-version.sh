#! /usr/bin/env bash

set -e

readonly requested_jdk="$1"

if [[ -z "$requested_jdk" ]]; then
    echo
    echo "Error : Missing requested JDK version"
    echo
    echo "Usage :"
    echo "‣ ./normalize-jdk-version jdk11"
    echo "‣ ./normalize-jdk-version jdk17"
    echo
    exit 1
fi

case "$requested_jdk" in
    "jdk11" | "jdk17")
        jdk_version=$(echo "$requested_jdk" | sed "s/jdk//g")
        echo "::set-output name=jdk::$jdk_version"
        ;;
    "*")
        echo "Invalid jdk name -> $requested_jdk"
        exit 1
        ;;
esac
