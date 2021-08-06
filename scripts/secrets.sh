#!/usr/bin/env bash

set -e

readonly operation="$1"
readonly password="$2"

setup() {
	DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )";
	cd "${DIR%/*}"
}

encrypt() {
	openssl aes-256-cbc -salt -a -e -pass pass:$1 -in dotanuki-demos.jks -out signing/keystore.enc
	openssl aes-256-cbc -salt -a -e  -pass pass:$1 -in signing.properties -out signing/credentials.enc
}

decrypt() {
	openssl aes-256-cbc -salt -a -e -pass pass:$1 -d -in signing/keystore.enc -out dotanuki-demos.jks
	openssl aes-256-cbc -salt -a -e -pass pass:$1 -d -in signing/credentials.enc -out signing.properties
}

setup

case "$operation" in
    "encrypt")
        encrypt $password
        ;;
    "decrypt")
        decrypt $password
        ;;
    *)
        echo
        echo "Error: unsupported operation → $operation"
        echo "Should pick one from : 'encrypt' or 'decrypt'"
        echo
        exit 1
        ;;
esac
