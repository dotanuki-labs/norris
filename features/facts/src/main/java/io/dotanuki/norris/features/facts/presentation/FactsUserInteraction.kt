package io.dotanuki.norris.features.facts.presentation

sealed class FactsUserInteraction {
    object OpenedScreen : FactsUserInteraction()
    object RequestedFreshContent : FactsUserInteraction()
}
