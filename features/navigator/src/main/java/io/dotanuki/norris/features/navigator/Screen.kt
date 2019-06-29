package io.dotanuki.norris.features.navigator

sealed class Screen {
    object FactsList : Screen()
    object SearchQuery : Screen()

    override fun toString() = when (this) {
        FactsList -> "Facts Screen"
        SearchQuery -> "SearchQuery Query Screen"
        else -> super.toString()
    }
}