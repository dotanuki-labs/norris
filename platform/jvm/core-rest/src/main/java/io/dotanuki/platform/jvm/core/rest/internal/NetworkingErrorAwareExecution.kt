package io.dotanuki.platform.jvm.core.rest.internal

internal object NetworkingErrorAwareExecution {
    suspend operator fun <T> invoke(target: suspend () -> T): T =
        try {
            target.invoke()
        } catch (incoming: Throwable) {
            throw HttpNetworkingErrorTransformer.transform(incoming)
        }
}
