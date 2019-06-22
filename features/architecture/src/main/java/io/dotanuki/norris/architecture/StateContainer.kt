package io.dotanuki.norris.architecture

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow

interface StateContainer<T> : ViewStateRegistry<T>, ViewStatesEmitter<T> {

    class Unbounded<T> : StateContainer<T> {

        private val broadcaster by lazy {
            ConflatedBroadcastChannel<ViewState<T>>()
        }

        override val emissionScope = MainScope()

        override fun observableStates() = broadcaster.asFlow()

        override fun current(): ViewState<T>? = broadcaster.valueOrNull

        override suspend fun store(state: ViewState<T>) {
            broadcaster.send(state)
        }
    }
}