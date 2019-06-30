package io.dotanuki.norris.domain.util

import org.assertj.core.api.Assertions.assertThat

fun unwrapCaughtError(result: Result<*>) =
    result.exceptionOrNull()
        ?.let { it }
        ?: throw IllegalArgumentException("Not an error")

fun assertErrorTransformed(expected: Throwable, whenRunning: () -> Any) {
    val result = runCatching { whenRunning.invoke() }
    val unwrapped = unwrapCaughtError(result)
    assertThat(unwrapped).isEqualTo(expected)
}