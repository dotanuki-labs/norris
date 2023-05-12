package io.dotanuki.features.search.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class SearchStateMachine(
    initialState: SearchScreenState,
    private val machineScope: CoroutineScope,
    private val stateProcessor: suspend (SearchInteraction) -> SearchScreenState
) {
    private val interactions: Channel<SearchInteraction> = Channel(UNLIMITED)
    private val states: MutableStateFlow<SearchScreenState> = MutableStateFlow(initialState)

    init {
        machineScope.launch {
            interactions.consumeAsFlow().collect {
                states.value = stateProcessor(it)
            }
        }
    }

    fun observe() = states.asStateFlow()

    fun process(
        interaction: SearchInteraction,
        temporaryState: SearchScreenState? = null
    ) {
        machineScope.launch {
            temporaryState?.let { states.value = it }
            interactions.send(interaction)
        }
    }
}
