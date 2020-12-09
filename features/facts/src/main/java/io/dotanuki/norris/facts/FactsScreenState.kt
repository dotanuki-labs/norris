package io.dotanuki.norris.facts

sealed class FactsScreenState {
    object Idle : FactsScreenState()
    object Loading : FactsScreenState()
    data class Success(val value: FactsPresentation) : FactsScreenState()
    data class Failed(val reason: Throwable) : FactsScreenState()
}