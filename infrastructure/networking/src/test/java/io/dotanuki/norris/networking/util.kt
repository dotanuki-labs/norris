package io.dotanuki.norris.networking

import io.dotanuki.norris.rest.errors.ErrorTransformer
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

fun unwrapCaughtError(result: Result<*>) =
    result.exceptionOrNull()
        ?.let { it }
        ?: throw IllegalArgumentException("Not an error")

suspend fun emulateErrorAtSuspendableOperation(error: Throwable) =
    suspendCoroutine<Unit> { continuation ->
        continuation.resumeWithException(error)
    }

fun assertTransformed(from: Throwable, to: Throwable, using: ErrorTransformer) =
    runBlocking {
        val result = runCatching { emulateErrorAtSuspendableOperation(from) }
        val unwrapped = unwrapCaughtError(result)
        val transformed = using.transform(unwrapped)
        assertThat(transformed).isEqualTo(to)
    }
