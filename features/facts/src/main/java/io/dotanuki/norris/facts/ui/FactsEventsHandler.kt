package io.dotanuki.norris.facts.ui

import android.util.Log
import io.dotanuki.norris.facts.presentation.FactsScreenState
import io.dotanuki.norris.facts.presentation.FactsUserInteraction
import io.dotanuki.norris.facts.presentation.FactsViewModel
import io.dotanuki.norris.navigator.Navigator
import io.dotanuki.norris.navigator.Screen

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
