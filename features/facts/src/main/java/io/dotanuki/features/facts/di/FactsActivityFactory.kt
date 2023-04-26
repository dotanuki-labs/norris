package io.dotanuki.features.facts.di

import io.dotanuki.features.facts.ui.FactsActivity
import io.dotanuki.platform.android.core.persistance.di.LocalStorageFactory
import io.dotanuki.platform.jvm.core.rest.di.ApiUrlFactory
import io.dotanuki.platform.jvm.core.rest.di.ChuckNorrisServiceClientFactory

object FactsActivityFactory {

    context (ApiUrlFactory)
    fun create(): FactsActivity =
        with(ChuckNorrisServiceClientFactory) {
            with(LocalStorageFactory) {
                with(FactsViewModelFactory()) {
                    FactsActivity()
                }
            }
        }
}
