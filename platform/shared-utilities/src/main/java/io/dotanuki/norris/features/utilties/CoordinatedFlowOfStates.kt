package io.dotanuki.norris.features.utilties

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

class CoordinatedFlowOfStates<State>(
    private val viewModel: ViewModel,
    private val initialState: State
) {

    private val internalStates = MutableStateFlow(initialState)

    fun expose(): StateFlow<State> = internalStates.asStateFlow().stateIn(
        scope = viewModel.viewModelScope,
        started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
        initialValue = initialState
    )

    fun update(newValue: State) {
        internalStates.value = newValue
    }

    companion object {
        const val SUBSCRIPTION_TIMEOUT = 5000L
    }
}
