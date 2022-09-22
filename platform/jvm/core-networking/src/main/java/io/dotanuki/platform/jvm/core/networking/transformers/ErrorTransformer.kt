package io.dotanuki.platform.jvm.core.networking.transformers

interface ErrorTransformer {

    suspend fun transform(incoming: Throwable): Throwable
}
