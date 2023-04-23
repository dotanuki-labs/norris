package io.dotanuki.features.facts.ui

import io.dotanuki.features.facts.presentation.FactsUserInteraction
import io.dotanuki.features.facts.presentation.FactsViewModel
import io.dotanuki.platform.android.core.navigator.Navigator
import io.dotanuki.platform.android.core.navigator.Screen

interface FactsEventsHandler {

    fun onRefresh()

    fun onSearch()

    fun onShare(fact: String)

    object NoOp : FactsEventsHandler {

        override fun onRefresh() = Unit

        override fun onSearch() = Unit

        override fun onShare(fact: String) = Unit
    }

    class Unidirectional(
        private val viewModel: FactsViewModel,
        private val navigator: Navigator
    ) : FactsEventsHandler {

        override fun onRefresh() {
            viewModel.handle(FactsUserInteraction.RequestedFreshContent)
        }

        override fun onSearch() {
            navigator.navigateTo(Screen.SearchQuery)
        }

        override fun onShare(fact: String) {
            navigator.toSharingApp(fact, "Share this fact!")
        }
    }
}
