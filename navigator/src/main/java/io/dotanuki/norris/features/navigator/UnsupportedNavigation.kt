package io.dotanuki.norris.features.navigator

class UnsupportedNavigation(destination: Screen) : RuntimeException(
    "Cannot navigate to this destination -> $destination"
)