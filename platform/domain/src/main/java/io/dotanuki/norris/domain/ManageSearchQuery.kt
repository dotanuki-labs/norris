package io.dotanuki.norris.domain

import io.dotanuki.norris.domain.services.SearchesHistoryService

class ManageSearchQuery(private val historyService: SearchesHistoryService) {

    suspend fun save(query: String) {
        historyService.registerNewSearch(query)
    }

    suspend fun actualQuery() =
        with(historyService.lastSearches()) {
            if (isEmpty()) FALLBACK else first()
        }

    companion object {
        const val FALLBACK = "code"
    }
}
