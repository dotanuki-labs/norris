package io.dotanuki.norris.domain.errors

interface ErrorTransformer {

    suspend fun transform(incoming: Throwable): Throwable
}
