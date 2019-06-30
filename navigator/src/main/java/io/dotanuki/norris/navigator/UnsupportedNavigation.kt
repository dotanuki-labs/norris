package io.dotanuki.norris.navigator

class UnsupportedNavigation(destination: Screen) : RuntimeException(
    "Cannot navigate to this destination -> $destination"
)