package io.dotanuki.features.search.ui

import io.dotanuki.features.search.presentation.SearchInteraction
import io.dotanuki.features.search.presentation.SearchViewModel

interface SearchEventsHandler {

    fun onChipClicked(term: String)

    fun onNewSearch(term: String)

    object NoOp : SearchEventsHandler {
        override fun onChipClicked(term: String) = Unit

        override fun onNewSearch(term: String) = Unit
    }

    class Unidirectional(private val viewModel: SearchViewModel) : SearchEventsHandler {
        override fun onNewSearch(term: String) {
            viewModel.handle(SearchInteraction.NewQuerySet(term))
        }

        override fun onChipClicked(term: String) {
            viewModel.handle(SearchInteraction.NewQuerySet(term))
        }
    }
}
