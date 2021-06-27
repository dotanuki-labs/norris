package io.dotanuki.norris.search.presentation

sealed class SearchInteraction {
    object OpenedScreen : SearchInteraction()
    data class QueryFieldChanged(val query: String) : SearchInteraction()
}
