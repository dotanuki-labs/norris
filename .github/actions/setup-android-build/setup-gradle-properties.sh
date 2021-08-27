#! /usr/bin/env bash

set -e

readonly workdir="$GITHUB_WORKSPACE"
readonly target="$RUNNER_OS"
readonly gha_path=".github/actions/setup-android-build"

mv ./"$gha_path"/gradle-"$target".properties "$workdir"/gradle.properties
