package io.dotanuki.features.facts.presentation

sealed class FactsScreenState {
    object Idle : FactsScreenState()

    object Loading : FactsScreenState()

    object Empty : FactsScreenState()

    data class Success(
        val value: FactsPresentation,
    ) : FactsScreenState()

    data class Failed(
        val reason: Throwable,
    ) : FactsScreenState()
}
