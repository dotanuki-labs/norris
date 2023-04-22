package io.dotanuki.features.facts.di

import io.dotanuki.features.facts.data.ActualSearchDataSource
import io.dotanuki.features.facts.data.FactsDataSource
import io.dotanuki.platform.android.core.persistance.di.PersistanceModule
import io.dotanuki.platform.jvm.core.rest.di.RestServiceModule

class FactsModule(
    val persistanceModule: PersistanceModule,
    val restServiceModule: RestServiceModule
) {

    val vmFactory by lazy {
        val actualSearchDataSource = ActualSearchDataSource(persistanceModule.localStorage)
        val factsDataSource = FactsDataSource(restServiceModule.chuckNorrisServiceClient)
        FactsViewModelFactory(factsDataSource, actualSearchDataSource)
    }
}
