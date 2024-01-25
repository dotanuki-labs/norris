package io.dotanuki.features.facts.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class FactsStateMachine(
    initialState: FactsScreenState,
    private val machineScope: CoroutineScope,
    private val stateProcessor: suspend () -> FactsScreenState,
) {
    private val interactions: Channel<FactsUserInteraction> = Channel(UNLIMITED)
    private val states: MutableStateFlow<FactsScreenState> = MutableStateFlow(initialState)

    init {
        machineScope.launch {
            processInteraction(stateProcessor)
        }
    }

    fun observe() = states.asStateFlow()

    fun process(
        interaction: FactsUserInteraction,
        temporaryState: FactsScreenState? = null,
    ) {
        machineScope.launch {
            temporaryState?.let { states.value = it }
            interactions.send(interaction)
        }
    }

    private suspend fun processInteraction(processor: suspend () -> FactsScreenState) {
        interactions.consumeAsFlow().collect {
            states.value = processor()
        }
    }
}
