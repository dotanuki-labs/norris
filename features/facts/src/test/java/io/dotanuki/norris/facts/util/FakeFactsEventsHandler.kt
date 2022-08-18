package io.dotanuki.norris.facts.util

import io.dotanuki.norris.facts.presentation.FactsScreenState
import io.dotanuki.norris.facts.ui.FactsEventsHandler

class FakeFactsEventsHandler : FactsEventsHandler {

    val trackedStates = mutableListOf<FactsScreenState>()

    override fun postReceive(state: FactsScreenState) {
        trackedStates += state
    }

    override fun onRefresh() = Unit

    override fun onSearch() = Unit

    override fun onShare(fact: String) = Unit
}
