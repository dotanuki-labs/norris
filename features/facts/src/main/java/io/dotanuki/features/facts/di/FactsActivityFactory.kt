package io.dotanuki.features.facts.di

import io.dotanuki.features.facts.ui.FactsActivity
import io.dotanuki.platform.jvm.core.rest.di.ApiUrlFactory

object FactsActivityFactory {

    context (ApiUrlFactory)
    fun create(): FactsActivity =
        with(FactsContext.standard()) {
            FactsActivity()
        }
}
