package io.dotanuki.norris.domain

import io.dotanuki.norris.domain.errors.SearchFactsError.EmptyTerm
import io.dotanuki.norris.domain.errors.SearchFactsError.NoResultsFound
import io.dotanuki.norris.domain.services.RemoteFactsService
import io.dotanuki.norris.domain.services.SearchesHistoryService

class FetchFacts(
    private val factsService: RemoteFactsService,
    private val historyService: SearchesHistoryService
) {

    suspend fun search(term: String) =
        when {
            term.isEmpty() -> throw EmptyTerm
            else -> {
                val facts = factsService.fetchFacts(term)
                historyService.registerNewSearch(term)
                if (facts.isEmpty()) throw NoResultsFound else facts
            }
        }
}