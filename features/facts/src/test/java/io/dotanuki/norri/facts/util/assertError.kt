package io.dotanuki.norri.facts.util

import com.google.common.truth.Truth.assertThat

fun unwrapCaughtError(result: Result<*>) =
    result.exceptionOrNull() ?: throw IllegalArgumentException("Not an error")

fun assertErrorTransformed(expected: Throwable, whenRunning: () -> Any) {
    val result = runCatching { whenRunning.invoke() }
    val unwrapped = unwrapCaughtError(result)
    assertThat(unwrapped).isEqualTo(expected)
}
