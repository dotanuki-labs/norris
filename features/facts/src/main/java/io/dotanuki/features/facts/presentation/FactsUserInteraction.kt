package io.dotanuki.features.facts.presentation

sealed class FactsUserInteraction {
    object OpenedScreen : FactsUserInteraction()
    object RequestedFreshContent : FactsUserInteraction()
}
