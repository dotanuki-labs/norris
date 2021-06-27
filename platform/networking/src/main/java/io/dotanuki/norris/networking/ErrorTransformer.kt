package io.dotanuki.norris.networking

interface ErrorTransformer {

    suspend fun transform(incoming: Throwable): Throwable
}
