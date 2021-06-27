package io.dotanuki.norris.facts.presentation

sealed class FactsUserInteraction {
    object OpenedScreen : FactsUserInteraction()
    object RequestedFreshContent : FactsUserInteraction()
}
