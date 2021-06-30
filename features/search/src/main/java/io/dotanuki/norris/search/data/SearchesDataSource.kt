package io.dotanuki.norris.search.data

import io.dotanuki.norris.persistance.LocalStorage
import io.dotanuki.norris.rest.ChuckNorrisDotIO
import io.dotanuki.norris.search.domain.SearchOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchesDataSource(
    private val storage: LocalStorage,
    private val api: ChuckNorrisDotIO
) {

    suspend fun searchOptions(): SearchOptions = withContext(Dispatchers.IO) {
        SearchOptions(
            recommendations = api.categories().raw,
            history = storage.lastSearches()
        )
    }

    suspend fun saveNewSearch(term: String) = withContext(Dispatchers.IO) {
        storage.registerNewSearch(term)
    }
}
