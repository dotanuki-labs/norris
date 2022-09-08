#! /usr/bin/env bash

set -e

readonly requested_jdk="$1"

case "$requested_jdk" in
    "jdk11" | "jdk17")
        local jdk_version=$(echo "$requested_jdk" | sed "s/jdk//g")
        echo "::set-output name=jdk::$jdk_version"
        ;;
    "*")
        echo "Invalid jdk name -> $requested_jdk"
        exit 1
        ;;
esac
