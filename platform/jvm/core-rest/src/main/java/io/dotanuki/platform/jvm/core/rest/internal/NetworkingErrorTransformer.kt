package io.dotanuki.platform.jvm.core.rest.internal

internal interface NetworkingErrorTransformer {
    fun transform(incoming: Throwable): Throwable
}
