package io.dotanuki.platform.jvm.core.networking.transformers

import kotlinx.coroutines.runBlocking
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CheckErrorTransformation(
    private val original: Throwable,
    private val transformer: ErrorTransformer
) {
    private fun runCheck(check: (Throwable) -> Unit) =
        runBlocking {
            runCatching { errorAtSuspendableOperation(original) }
                .onFailure { check(transformer.transform(it)) }
                .onSuccess { throw AssertionError("Not an error") }
        }

    private suspend fun errorAtSuspendableOperation(error: Throwable) =
        suspendCoroutine<Unit> { continuation ->
            continuation.resumeWithException(error)
        }

    companion object {
        fun checkTransformation(from: Throwable, using: ErrorTransformer, check: (Throwable) -> Unit) =
            CheckErrorTransformation(from, using).runCheck { check.invoke(it) }
    }
}
