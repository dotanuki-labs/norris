package io.dotanuki.features.search.di

import io.dotanuki.features.search.ui.SearchActivity
import io.dotanuki.platform.jvm.core.rest.di.ApiUrlFactory

object SearchActivityFactory {
    context (ApiUrlFactory)
    fun create(): SearchActivity =
        with(SearchContext.standard()) {
            SearchActivity()
        }
}
