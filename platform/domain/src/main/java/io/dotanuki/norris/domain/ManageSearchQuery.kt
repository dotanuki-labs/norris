package io.dotanuki.norris.domain

import io.dotanuki.norris.domain.services.SearchesHistoryService

class ManageSearchQuery(private val historyService: SearchesHistoryService) {

    fun save(query: String) {
        historyService.registerNewSearch(query)
    }

    suspend fun actualQuery(): String =
        with(historyService.lastSearches()) {
            if (isEmpty()) FALLBACK else last()
        }

    companion object {
        const val FALLBACK = ""
    }
}
