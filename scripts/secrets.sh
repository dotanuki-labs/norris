#!/usr/bin/env bash

set -e

readonly operation="$1"
readonly password="$2"

setup() {
	DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
	cd "${DIR%/*}"
}

encript_with_gpg() {
	echo "$1" |
		gpg --batch --yes --passphrase-fd 0 --cipher-algo aes256 --symmetric --output "$2" "$3"
}

decriypt_with_gpg() {
	echo "$1" |
		gpg --decrypt --batch --yes --passphrase-fd 0 --output "$2" "$3"
}

encrypt() {
	encript_with_gpg "$1" signing/dotanuki-demos.gpg dotanuki-demos.jks
	encript_with_gpg "$1" signing/credentials.gpg signing.properties
}

decrypt() {
	decriypt_with_gpg "$1" dotanuki-demos.jks signing/dotanuki-demos.gpg
	decriypt_with_gpg "$1" signing.properties signing/credentials.gpg
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
