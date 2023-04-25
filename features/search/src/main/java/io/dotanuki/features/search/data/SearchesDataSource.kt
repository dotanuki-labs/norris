package io.dotanuki.features.search.data

import io.dotanuki.features.search.domain.SearchOptions
import io.dotanuki.platform.android.core.persistance.LocalStorage
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient

class SearchesDataSource(
    private val localStorage: LocalStorage,
    private val norrisClient: ChuckNorrisServiceClient
) {

    suspend fun searchOptions(): SearchOptions =
        SearchOptions(
            recommendations = norrisClient.categories(),
            history = localStorage.lastSearches()
        )

    fun saveNewSearch(term: String) =
        localStorage.registerNewSearch(term)
}
