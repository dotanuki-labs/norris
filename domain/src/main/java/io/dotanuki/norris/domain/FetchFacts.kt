package io.dotanuki.norris.domain

import io.dotanuki.norris.domain.errors.SearchFactsError.EmptyTerm
import io.dotanuki.norris.domain.errors.SearchFactsError.NoResultsFound
import io.dotanuki.norris.domain.services.RemoteFactsService

class FetchFacts(
    private val factsService: RemoteFactsService
) {

    suspend fun search(term: String) =
        when {
            term.isEmpty() -> throw EmptyTerm
            else -> {
                val facts = factsService.fetchFacts(term)
                if (facts.isEmpty()) throw NoResultsFound else facts
            }
        }
}