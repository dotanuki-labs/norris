package io.dotanuki.norris.domain.services

interface SearchesHistoryService {

    suspend fun lastSearches(): List<String>

    suspend fun registerNewSearch(term: String)
}