package io.dotanuki.coroutines.testutils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun <T> Flow<T>.test(scope: CoroutineScope, block: FlowTest<T>.() -> Unit) {
    val emissions = mutableListOf<T>()
    val job = scope.launch { toList(emissions) }
    FlowTest(job, emissions).apply(block)
}

class FlowTest<T>(private val parentJob: Job, private val emissions: List<T>) {

    fun triggerEmissions(action: suspend () -> Job) {
        runBlocking { action().join() }
    }

    fun afterCollect(verification: (List<T>) -> Unit) {
        parentJob.invokeOnCompletion {
            verification.invoke(emissions)
        }
    }

    companion object {
        fun <T> flowTest(
            target: Flow<T>,
            scope: CoroutineScope = GlobalScope,
            block: FlowTest<T>.() -> Unit
        ) {
            target.test(scope, block)
        }
    }
}