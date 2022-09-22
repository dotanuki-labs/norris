package io.dotanuki.norris.features.facts.ui

import android.util.Log
import io.dotanuki.norris.features.facts.presentation.FactsScreenState
import io.dotanuki.norris.features.facts.presentation.FactsUserInteraction
import io.dotanuki.norris.features.facts.presentation.FactsViewModel
import io.dotanuki.platform.android.core.navigator.Navigator
import io.dotanuki.platform.android.core.navigator.Screen

interface FactsEventsHandler {

    fun postReceive(state: FactsScreenState)

    fun onRefresh()

    fun onSearch()

    fun onShare(fact: String)

    object NoOp : FactsEventsHandler {
        override fun postReceive(state: FactsScreenState) = Unit

        override fun onRefresh() = Unit

        override fun onSearch() = Unit

        override fun onShare(fact: String) = Unit
    }

    class Unidirectional(
        private val viewModel: FactsViewModel,
        private val navigator: Navigator
    ) : FactsEventsHandler {
        override fun postReceive(state: FactsScreenState) {
            Log.d("FactsScreen", "Processed -> ${state.javaClass.simpleName}")
        }

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
