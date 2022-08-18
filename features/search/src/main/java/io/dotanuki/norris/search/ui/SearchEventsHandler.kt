package io.dotanuki.norris.search.ui

import android.util.Log
import io.dotanuki.norris.search.presentation.SearchInteraction
import io.dotanuki.norris.search.presentation.SearchScreenState
import io.dotanuki.norris.search.presentation.SearchViewModel

interface SearchEventsHandler {

    fun postReceive(state: SearchScreenState)

    fun onChipClicked(term: String)

    fun onNewSearch(term: String)

    object NoOp : SearchEventsHandler {
        override fun postReceive(state: SearchScreenState) = Unit

        override fun onChipClicked(term: String) = Unit

        override fun onNewSearch(term: String) = Unit
    }

    class Unidirectional(private val viewModel: SearchViewModel) : SearchEventsHandler {
        override fun postReceive(state: SearchScreenState) {
            Log.d("SearchScreen", "Processed -> ${state.javaClass.simpleName}")
        }

        override fun onNewSearch(term: String) {
            viewModel.handle(SearchInteraction.NewQuerySet(term))
        }

        override fun onChipClicked(term: String) {
            viewModel.handle(SearchInteraction.NewQuerySet(term))
        }
    }
}
