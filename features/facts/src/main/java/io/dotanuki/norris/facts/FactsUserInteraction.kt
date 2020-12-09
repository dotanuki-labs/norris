package io.dotanuki.norris.facts

sealed class FactsUserInteraction {
    object OpenedScreen : FactsUserInteraction()
    object RequestedFreshContent : FactsUserInteraction()
}