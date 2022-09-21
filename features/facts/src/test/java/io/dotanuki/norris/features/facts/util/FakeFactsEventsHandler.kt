package io.dotanuki.norris.features.facts.util

import io.dotanuki.norris.features.facts.presentation.FactsScreenState
import io.dotanuki.norris.features.facts.ui.FactsEventsHandler

class FakeFactsEventsHandler : FactsEventsHandler {

    val trackedStates = mutableListOf<FactsScreenState>()

    override fun postReceive(state: FactsScreenState) {
        trackedStates += state
    }

    override fun onRefresh() = Unit

    override fun onSearch() = Unit

    override fun onShare(fact: String) = Unit
}
