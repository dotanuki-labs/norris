package io.dotanuki.norris.facts.util

import io.dotanuki.norris.facts.ui.FactsEventsHandler
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
