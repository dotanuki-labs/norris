#! /usr/bin/env bash

set -e

mkdir -p $HOME/.android

keytool\
    -dname "cn=Ubiratan Soares, ou=Dotanuki, o=Dotanuki, l=Barcelona, st=Catalonia, c=ES"\
    -genkey -v\
    -keystore "$HOME/.android/debug.keystore"\
    -storepass "android"\
    -alias "androiddebugkey"\
    -keypass "android"\
    -keyalg RSA\
    -keysize 2048\
    -validity 10000
