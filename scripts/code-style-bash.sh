#!/usr/bin/env bash

set -e

current_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

readonly current_dir
readonly scripts_dir="scripts"

readonly docker_shmft="docker.io/mvdan/shfmt:latest"
readonly docker_shellcheck="docker.io/koalaman/shellcheck:stable"

echo

if (! docker stats --no-stream >/dev/null); then
    echo "Docker required for quality checks"
    exit 1
fi

echo "🔥 Running quality checks"
echo

cd "${current_dir%/*}"

echo "→ Checking code style (shfmt)"
docker run --rm -v "$PWD:/mnt" -w /mnt "$docker_shmft" -d "$scripts_dir"
echo

echo "→ Checking code smells (shellcheck)"
cd "$scripts_dir"
docker run --rm -v "$PWD:/mnt" "$docker_shellcheck" -x ./*.sh
echo

echo "✅ All good!"
echo
