package io.dotanuki.features.search.data

import io.dotanuki.features.search.di.SearchContext
import io.dotanuki.features.search.domain.SearchOptions

context (SearchContext)
class SearchesDataSource {

    suspend fun searchOptions(): SearchOptions =
        SearchOptions(
            recommendations = restClient.categories(),
            history = localStorage.lastSearches()
        )

    fun saveNewSearch(term: String) =
        localStorage.registerNewSearch(term)
}
