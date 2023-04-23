package io.dotanuki.features.facts.di

import io.dotanuki.features.facts.data.ActualSearchDataSource
import io.dotanuki.features.facts.data.FactsDataSource
import io.dotanuki.platform.android.core.persistance.LocalStorage
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient

class FactsModule(
    private val localStorage: LocalStorage,
    private val chuckNorrisServiceClient: ChuckNorrisServiceClient
) {
    val vmFactory by lazy {
        val actualSearchDataSource = ActualSearchDataSource(localStorage)
        val factsDataSource = FactsDataSource(chuckNorrisServiceClient)
        FactsViewModelFactory(factsDataSource, actualSearchDataSource)
    }
}
