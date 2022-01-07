#! /usr/bin/env bash

set -e

setup() {
    dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    cd "${dir%/*}"
}

agent_version="2.7.58"
agent_releases="https://repo.thundra.io/service/local/repositories/thundra-releases"
agent_folder="content/io/thundra/agent/thundra-agent-bootstrap/${agent_version}"
agent_jar="thundra-agent-bootstrap-${agent_version}.jar"
agent_url="${agent_releases}/${agent_folder}/$agent_jar"

mkdir -p thundra
curl $agent_url --output "thundra/agent.jar"
