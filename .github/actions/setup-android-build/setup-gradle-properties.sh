#! /usr/bin/env bash

set -e

readonly workdir="$GITHUB_WORKSPACE"
readonly target="$RUNNER_OS,,"
readonly gha_path=".github/actions/setup-android-build"

mv "$workdir"/"$target".properties "$workdir"/gradle.properties
