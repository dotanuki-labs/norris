package io.dotanuki.norris.search

sealed class SearchInteraction {
    object OpenedScreen : SearchInteraction()
    data class QueryFieldChanged(val query: String) : SearchInteraction()
}