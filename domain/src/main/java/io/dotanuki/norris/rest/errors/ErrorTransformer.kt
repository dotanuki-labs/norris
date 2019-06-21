package io.dotanuki.norris.rest.errors

interface ErrorTransformer {

    suspend fun transform(incoming: Throwable): Throwable
}