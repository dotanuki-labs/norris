package io.dotanuki.norris.search.data

import io.dotanuki.norris.persistance.LocalStorage
import io.dotanuki.norris.rest.ChuckNorrisDotIO
import io.dotanuki.norris.search.domain.SearchOptions

class SearchesDataSource(
    private val storage: LocalStorage,
    private val api: ChuckNorrisDotIO
) {

    suspend fun searchOptions(): SearchOptions =
        SearchOptions(
            recommendations = api.categories().raw,
            history = storage.lastSearches()
        )

    fun saveNewSearch(term: String) {
        storage.registerNewSearch(term)
    }
}
