package io.dotanuki.features.search.di

import io.dotanuki.platform.android.core.persistance.LocalStorage
import io.dotanuki.platform.android.core.persistance.di.LocalStorageFactory
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient
import io.dotanuki.platform.jvm.core.rest.di.ApiUrlFactory
import io.dotanuki.platform.jvm.core.rest.di.ChuckNorrisServiceClientFactory

internal data class SearchContext(
    val restClient: ChuckNorrisServiceClient,
    val localStorage: LocalStorage
) {

    companion object {

        context (ApiUrlFactory)
        fun standard() = SearchContext(
            restClient = ChuckNorrisServiceClientFactory.create(),
            localStorage = LocalStorageFactory.create()
        )
    }
}
