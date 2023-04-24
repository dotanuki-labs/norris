package io.dotanuki.features.search.ui

interface SearchEventsHandler {

    fun onChipClicked(term: String)

    fun onNewSearch(term: String)

    object NoOp : SearchEventsHandler {
        override fun onChipClicked(term: String) = Unit

        override fun onNewSearch(term: String) = Unit
    }
}
