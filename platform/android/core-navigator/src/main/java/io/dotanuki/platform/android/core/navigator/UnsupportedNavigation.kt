package io.dotanuki.platform.android.core.navigator

data class UnsupportedNavigation(
    val destination: Screen,
) : RuntimeException(
        "Cannot navigate to this destination -> $destination"
    )
