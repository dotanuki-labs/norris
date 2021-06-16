package io.dotanuki.norris.networking

import io.dotanuki.norris.domain.errors.ErrorTransformer
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CheckErrorTransformation(
    private val original: Throwable,
    private val transformer: ErrorTransformer
) {
    private fun runCheck(check: (Throwable) -> Unit) =
        runBlocking {
            val result = runCatching { errorAtSuspendableOperation(original) }
            val unwrapped = unwrapError(result)
            val transformed = transformer.transform(unwrapped)
            check(transformed)
        }

    private suspend fun errorAtSuspendableOperation(error: Throwable) =
        suspendCoroutine<Unit> { continuation ->
            continuation.resumeWithException(error)
        }

    private fun unwrapError(result: Result<*>) =
        result.exceptionOrNull() ?: throw IllegalArgumentException("Not an error")

    companion object {
        fun checkTransformation(from: Throwable, using: ErrorTransformer, check: (Throwable) -> Unit) =
            CheckErrorTransformation(from, using).runCheck { check.invoke(it) }
    }
}
