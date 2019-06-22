package io.dotanuki.coroutines.testutils

import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

fun unwrapError(result: Result<*>) =
    result.exceptionOrNull()
        ?.let { it }
        ?: throw IllegalArgumentException("Not an error")

suspend fun <T> errorAtSuspendableOperation(error: Throwable) =
    suspendCoroutine<T> { continuation ->
        continuation.resumeWithException(error)
    }