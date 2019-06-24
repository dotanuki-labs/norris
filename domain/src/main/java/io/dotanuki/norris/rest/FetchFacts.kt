package io.dotanuki.norris.rest

import io.dotanuki.norris.rest.services.RemoteFactsService

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