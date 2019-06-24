package io.dotanuki.norris.domain

import io.dotanuki.norris.domain.services.RemoteFactsService

class FetchFacts(private val service: RemoteFactsService) {

    suspend fun randomFacts() = with(service) {
        val randomCategory = availableCategories().random().name
        search(randomCategory)
    }

    suspend fun search(term: String) =
        when {
            term.isEmpty() -> throw UnsearchableTerm
            else -> service.fetchFacts(term)
        }
}