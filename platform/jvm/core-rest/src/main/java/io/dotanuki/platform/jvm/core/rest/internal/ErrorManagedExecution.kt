package io.dotanuki.platform.jvm.core.rest.internal

internal object ErrorManagedExecution {
    suspend operator fun <T> invoke(target: suspend () -> T): T =
        try {
            target.invoke()
        } catch (incoming: Throwable) {
            throw ManagedErrorTransformer.transform(incoming)
        }
}
