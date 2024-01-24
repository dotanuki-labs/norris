package io.dotanuki.features.facts.ui

interface FactsEventsHandler {
    fun onRefresh()

    fun onSearch()

    fun onShare(fact: String)

    object NoOp : FactsEventsHandler {
        override fun onRefresh() = Unit

        override fun onSearch() = Unit

        override fun onShare(fact: String) = Unit
    }
}
