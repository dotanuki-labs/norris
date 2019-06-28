package io.dotanuki.norris.domain

import io.dotanuki.norris.domain.model.SearchOptions
import io.dotanuki.norris.domain.services.RemoteFactsService
import io.dotanuki.norris.domain.services.SearchesHistoryService

class ComposeSearchOptions(
    private val searches: SearchesHistoryService,
    private val facts: RemoteFactsService
) {

    suspend fun execute() =
        SearchOptions(
            history = searches.lastSearches(),
            recommendations = facts.availableCategories().map { it.name }
        )
}