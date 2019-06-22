package io.dotanuki.norris.architecture

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

fun <T> Flow<T>.collectForTesting(scope: CoroutineScope = MainScope()): MutableList<T> {
    val states = mutableListOf<T>()
    scope.launch { toList(states) }
    return states
}