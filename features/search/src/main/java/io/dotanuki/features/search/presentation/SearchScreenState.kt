package io.dotanuki.features.search.presentation

sealed class SearchScreenState {
    object Idle : SearchScreenState()

    object Loading : SearchScreenState()

    data class Failed(val error: Throwable) : SearchScreenState()

    data class Success(
        val suggestions: List<String>,
        val history: List<String>,
    ) : SearchScreenState()

    object Done : SearchScreenState()
}
