package io.dotanuki.features.search.di

import io.dotanuki.features.search.data.SearchesDataSource
import io.dotanuki.platform.android.core.persistance.di.PersistanceModule
import io.dotanuki.platform.jvm.core.rest.di.RestServiceModule

class SearchModule(
    private val persistanceModule: PersistanceModule,
    private val restServiceModule: RestServiceModule
) {

    val vmFactory by lazy {
        val dataSource = SearchesDataSource(
            persistanceModule.localStorage,
            restServiceModule.chuckNorrisServiceClient
        )
        SearchViewModelFactory(dataSource)
    }
}
