package io.dotanuki.norris.domain

import io.dotanuki.norris.domain.model.SearchOptions
import io.dotanuki.norris.domain.services.SearchesHistoryService

class ComposeSearchOptions(
    private val searches: SearchesHistoryService,
    private val categories: FetchCategories
) {

    suspend fun execute() =
        SearchOptions(
            history = searches.lastSearches(),
            recommendations = categories.execute().map { it.name }
        )
}