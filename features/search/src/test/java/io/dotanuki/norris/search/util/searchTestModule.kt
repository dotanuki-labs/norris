package io.dotanuki.norris.search.util

import io.dotanuki.norris.search.ui.SearchEventsHandler
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val searchTestModule = DI.Module("search-test-module") {
    bind<SearchEventsHandler>(overrides = true) {
        provider {
            FakeSearchEventsHandler(instance())
        }
    }
}
