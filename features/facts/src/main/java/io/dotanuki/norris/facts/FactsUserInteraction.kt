package io.dotanuki.norris.facts

sealed class FactsUserInteraction {
    object OpenedScreen : FactsUserInteraction()
    object RequestedFreshContent : FactsUserInteraction()
    data class DefinedNewSearch(val query: String) : FactsUserInteraction()
}