package io.dotanuki.norri.facts.util

import io.dotanuki.norris.facts.ui.FactsScreen
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val factsTestModule = DI.Module("facts-test-module") {

    bind<FactsScreen>(overrides = true) {
        // Make sure the same instance is provided to both FactsActivity and Tests
        singleton {
            FakeFactsScreen()
        }
    }
}
