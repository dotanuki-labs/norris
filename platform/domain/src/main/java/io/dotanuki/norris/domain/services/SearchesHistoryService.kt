package io.dotanuki.norris.domain.services

interface SearchesHistoryService {

    suspend fun lastSearches(): List<String>

    fun registerNewSearch(term: String)
}
