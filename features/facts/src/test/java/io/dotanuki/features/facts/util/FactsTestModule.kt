package io.dotanuki.features.facts.util

import io.dotanuki.features.facts.ui.FactsEventsHandler
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider

val factsTestModule = DI.Module("facts-test-module") {

    bind<FactsEventsHandler>(overrides = true) {
        provider {
            FakeFactsEventsHandler()
        }
    }
}
