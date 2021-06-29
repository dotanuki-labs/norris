package io.dotanuki.norris.search.presentation

sealed class SearchInteraction {
    object OpenedScreen : SearchInteraction()
    data class SuggestionSelected(val query: String) : SearchInteraction()
    data class NewQuerySet(val query: String) : SearchInteraction()
}
