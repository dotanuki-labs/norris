package io.dotanuki.features.search.di

import io.dotanuki.platform.android.core.persistance.LocalStorage
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient

internal data class SearchContext(
    val restClient: ChuckNorrisServiceClient,
    val localStorage: LocalStorage
)
