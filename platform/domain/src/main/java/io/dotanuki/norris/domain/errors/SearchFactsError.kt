package io.dotanuki.norris.domain.errors

sealed class SearchFactsError(message: String) : Throwable(message) {
    object EmptyTerm : SearchFactsError("Search term can not be empty")
    object NoResultsFound : SearchFactsError("No results found for this search")
}
