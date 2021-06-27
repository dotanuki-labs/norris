package io.dotanuki.norris.facts.data

import io.dotanuki.norris.persistance.LocalStorage

class LatestSearchesDataSource(private val storage: LocalStorage) {

    fun save(query: String) {
        storage.registerNewSearch(query)
    }

    suspend fun actualQuery(): String =
        with(storage.lastSearches()) {
            if (isEmpty()) FALLBACK else last()
        }

    companion object {
        const val FALLBACK = ""
    }
}
