package io.dotanuki.norris.architecture

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

interface TaskExecutor {

    fun execute(block: suspend TaskExecutor.() -> Unit): Job

    class Concurrent(
        private val scope: CoroutineScope,
        private val dispatcher: CoroutineDispatcher
    ) : TaskExecutor {
        override fun execute(block: suspend TaskExecutor.() -> Unit) =
            scope.launch(dispatcher) {
                block.invoke(this@Concurrent)
            }
    }

    object Synchronous : TaskExecutor {
        override fun execute(block: suspend TaskExecutor.() -> Unit) = runBlocking {
            GlobalScope.launch {
                block.invoke(this@Synchronous)
            }
        }
    }
}