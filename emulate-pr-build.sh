#!/bin/sh

set -ex

act push -P ubuntu-latest=nektos/act-environments-ubuntu:18.04-full --env-file act.env