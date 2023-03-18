package io.dotanuki.features.search.data

import io.dotanuki.features.search.domain.SearchOptions
import io.dotanuki.platform.android.core.persistance.LocalStorage
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchesDataSource(
    private val localStorage: LocalStorage,
    private val norrisService: ChuckNorrisService
) {

    suspend fun searchOptions(): SearchOptions = withContext(Dispatchers.IO) {
        SearchOptions(
            recommendations = norrisService.categories().raw,
            history = localStorage.lastSearches()
        )
    }

    suspend fun saveNewSearch(term: String) = withContext(Dispatchers.IO) {
        localStorage.registerNewSearch(term)
    }
}
