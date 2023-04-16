package io.dotanuki.platform.jvm.core.networking.transformers

interface ErrorTransformer {

    fun transform(incoming: Throwable): Throwable
}
