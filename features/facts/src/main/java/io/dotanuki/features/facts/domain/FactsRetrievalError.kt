package io.dotanuki.features.facts.domain

sealed class FactsRetrievalError(message: String) : Throwable(message) {
    object EmptyTerm : FactsRetrievalError("Search term can not be empty")
    object NoResultsFound : FactsRetrievalError("No results found for this search")
}
