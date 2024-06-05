package io.dotanuki.features.search.presentation

import io.dotanuki.features.search.domain.SearchOptions

sealed class SearchPresentation {
    data class Suggestions(
        val options: SearchOptions,
    ) : SearchPresentation()

    data class QueryValidation(
        val valid: Boolean,
    ) : SearchPresentation()
}
