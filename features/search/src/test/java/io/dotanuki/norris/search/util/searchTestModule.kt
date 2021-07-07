package io.dotanuki.norris.search.util

import io.dotanuki.norris.search.ui.SearchScreen
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

val searchTestModule = DI.Module("search-test-module") {
    bind<SearchScreen>(overrides = true) {
        singleton {
            FakeSearchScreen()
        }
    }
}
