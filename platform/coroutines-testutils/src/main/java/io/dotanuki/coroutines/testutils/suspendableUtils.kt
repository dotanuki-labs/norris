package io.dotanuki.coroutines.testutils

import kotlinx.coroutines.runBlocking
import kotlin.properties.Delegates

fun unwrapError(result: Result<*>) =
    result.exceptionOrNull()
        ?.let { it }
        ?: throw IllegalArgumentException("Not an error")

class SuspendableErrorChecker<T> {

    private var result: Result<T> by Delegates.notNull()
    private var error: Throwable? = null
    private var target: T? = null

    fun take(block: suspend () -> T) {
        result = runBlocking {
            runCatching {
                target = block()
                target!!
            }
        }
    }

    fun once(block: suspend (T) -> Any) {
        runBlocking {
            target
                ?.let { tryExecution(block, it) }
                ?: throw IllegalArgumentException("Must provide take{...} block")
        }
    }

    fun check(assert: (Throwable) -> Unit) {
        error
            ?.let { assert.invoke(it) }
            ?: throw IllegalArgumentException("Must provide once{...} block")
    }

    private suspend fun tryExecution(block: suspend (T) -> Any, it: T) =
        try {
            block(it)
        } catch (incoming: Throwable) {
            error = incoming
        }

    companion object {
        fun <T> errorOnSuspendable(block: SuspendableErrorChecker<T>.() -> Unit) =
            SuspendableErrorChecker<T>().apply(block)
    }
}