package io.dotanuki.norris.facts.data

import io.dotanuki.norris.persistance.LocalStorage

class ActualSearchDataSource(private val storage: LocalStorage) {

    suspend fun actualQuery(): String =
        with(storage.lastSearches()) {
            if (isEmpty()) FALLBACK else last()
        }

    companion object {
        const val FALLBACK = ""
    }
}
