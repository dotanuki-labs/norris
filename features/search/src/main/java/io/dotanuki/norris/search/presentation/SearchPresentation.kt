package io.dotanuki.norris.search.presentation

import io.dotanuki.norris.search.domain.SearchOptions

sealed class SearchPresentation {
    data class Suggestions(val options: SearchOptions) : SearchPresentation()
    data class QueryValidation(val valid: Boolean) : SearchPresentation()
}
