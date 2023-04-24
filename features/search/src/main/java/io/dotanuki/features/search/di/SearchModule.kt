package io.dotanuki.features.search.di

import io.dotanuki.features.search.data.SearchesDataSource
import io.dotanuki.platform.android.core.persistance.LocalStorage
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient

class SearchModule(
    private val localStorage: LocalStorage,
    private val chuckNorrisServiceClient: ChuckNorrisServiceClient
) {
    val vmFactory by lazy {
        val dataSource = SearchesDataSource(localStorage, chuckNorrisServiceClient)
        SearchViewModelFactory(dataSource)
    }
}
