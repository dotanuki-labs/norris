package io.dotanuki.coroutines.testutils

fun unwrapError(result: Result<*>) =
    result.exceptionOrNull()
        ?.let { it }
        ?: throw IllegalArgumentException("Not an error")