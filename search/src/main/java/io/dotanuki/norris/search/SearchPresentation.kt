package io.dotanuki.norris.search

import io.dotanuki.norris.domain.model.SearchOptions

sealed class SearchPresentation {

    data class Suggestions(val options: SearchOptions) : SearchPresentation()
    data class QueryValidation(val valid: Boolean) : SearchPresentation()
}