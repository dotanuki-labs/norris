#!/usr/bin/env bash

set -e

readonly operation="$1"
readonly age_private_key="norris-key.txt"
readonly age_public_key="age1euw5h4datlkvmlmaw6vlu6pzw83kwnl83glkxhwqvfqysk8p0d2s72qnt4"

setup() {
    dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    cd "${dir%/*}"
}

require_age() {
    if ! which age >/dev/null; then
        echo "𝙓 Error : 'age' must be installed and available in your \$PATH"
        echo
        exit 1
    fi
}

require_private_key() {
    if ! test -f "$age_private_key"; then
        echo "𝙓 Error : 'norris-key.txt' required to perform crypto operations"
        echo
        exit 1
    fi
}

encrypt_with_age() {
    local plain_input="$1"
    local encrypted_output="$2"
    age -r "$age_public_key" <"$plain_input" >"$encrypted_output"
}

decrypt_with_age() {
    local encrypted_input="$1"
    local plain_output="$2"
    age --decrypt -i "$age_private_key" "$encrypted_input" >"$plain_output"
}

encrypt() {
    require_age
    require_private_key
    mkdir -p .config
    encrypt_with_age dotanuki-demos.jks .config/dotanuki-demos.age
    encrypt_with_age signing.properties .config/credentials.age
}

decrypt() {
    require_age
    require_private_key
    decrypt_with_age .config/dotanuki-demos.age dotanuki-demos.jks
    decrypt_with_age .config/credentials.age signing.properties
}

setup

case "$operation" in
"encrypt")
    encrypt
    ;;
"decrypt")
    decrypt
    ;;
*)
    echo
    echo "Error: unsupported operation → $operation"
    echo "Should pick one from : 'encrypt' or 'decrypt'"
    echo
    exit 1
    ;;
esac
