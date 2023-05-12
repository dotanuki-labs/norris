package io.dotanuki.features.facts.di

import io.dotanuki.platform.android.core.persistance.LocalStorage
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient

internal data class FactsContext(
    val restClient: ChuckNorrisServiceClient,
    val localStorage: LocalStorage
)
