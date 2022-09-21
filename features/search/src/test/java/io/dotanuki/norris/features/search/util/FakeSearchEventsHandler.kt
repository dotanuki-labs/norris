package io.dotanuki.norris.features.search.util

import io.dotanuki.norris.features.search.presentation.SearchInteraction
import io.dotanuki.norris.features.search.presentation.SearchScreenState
import io.dotanuki.norris.features.search.presentation.SearchViewModel
import io.dotanuki.norris.features.search.ui.SearchEventsHandler

class FakeSearchEventsHandler(private val viewModel: SearchViewModel) : SearchEventsHandler {

    val trackedStates = mutableListOf<SearchScreenState>()

    override fun postReceive(state: SearchScreenState) {
        trackedStates += state
    }

    override fun onNewSearch(term: String) {
        viewModel.handle(SearchInteraction.NewQuerySet(term))
    }

    override fun onChipClicked(term: String) {
        viewModel.handle(SearchInteraction.NewQuerySet(term))
    }
}
