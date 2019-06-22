package io.dotanuki.norris.rest

import io.dotanuki.norris.rest.services.RemoteFactsService

class FetchFacts(private val service: RemoteFactsService) {

    suspend fun randomFacts() = with(service) {
        val randomCategory = availableCategories().random().name
        fetchFacts(randomCategory)
    }

    suspend fun withTerm(term: String) = service.fetchFacts(term)
}