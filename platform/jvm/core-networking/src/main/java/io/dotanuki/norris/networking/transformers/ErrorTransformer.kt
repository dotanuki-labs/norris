package io.dotanuki.norris.networking.transformers

interface ErrorTransformer {

    suspend fun transform(incoming: Throwable): Throwable
}
