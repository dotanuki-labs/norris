package io.dotanuki.platform.android.core.persistance

interface LocalStorage {

    suspend fun lastSearches(): List<String>

    fun registerNewSearch(term: String)

    fun destroy()
}
