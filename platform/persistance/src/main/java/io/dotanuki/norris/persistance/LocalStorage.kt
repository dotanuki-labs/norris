package io.dotanuki.norris.persistance

interface LocalStorage {

    suspend fun lastSearches(): List<String>

    fun registerNewSearch(term: String)

    fun destroy()
}
