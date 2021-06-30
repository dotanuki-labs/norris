package io.dotanuki.norris.search.presentation

sealed class SearchScreenState {
    object Idle : SearchScreenState()
    object Loading : SearchScreenState()
    data class Error(val error: Throwable) : SearchScreenState()
    data class Content(
        val suggestions: List<String>,
        val history: List<String>
    ) : SearchScreenState()

    object Done : SearchScreenState()
}
