package io.dotanuki.features.search.di

import io.dotanuki.features.search.ui.SearchActivity
import io.dotanuki.platform.android.core.persistance.di.LocalStorageFactory
import io.dotanuki.platform.jvm.core.rest.di.ApiUrlFactory
import io.dotanuki.platform.jvm.core.rest.di.ChuckNorrisServiceClientFactory

object SearchActivityFactory {

    context (ApiUrlFactory)
    fun create(): SearchActivity =
        with(
            SearchContext(
                ChuckNorrisServiceClientFactory.create(),
                LocalStorageFactory.create()
            )
        ) {
            SearchActivity()
        }
}
