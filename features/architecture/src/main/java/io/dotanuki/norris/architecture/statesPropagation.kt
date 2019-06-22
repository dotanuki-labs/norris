package io.dotanuki.norris.architecture

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface ViewStatesEmitter<T> {

    fun observableStates(): Flow<ViewState<T>>

    val emissionScope: CoroutineScope
}

interface ViewStateRegistry<T> {

    fun current(): ViewState<T>?

    suspend fun store(state: ViewState<T>)
}