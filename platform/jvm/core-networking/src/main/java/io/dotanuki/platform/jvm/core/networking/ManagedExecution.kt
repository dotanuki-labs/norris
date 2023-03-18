package io.dotanuki.platform.jvm.core.networking

import io.dotanuki.platform.jvm.core.networking.transformers.HttpErrorTransformer
import io.dotanuki.platform.jvm.core.networking.transformers.NetworkingErrorTransformer
import io.dotanuki.platform.jvm.core.networking.transformers.SerializationErrorTransformer

suspend fun <T> managedExecution(target: suspend () -> T): T =
    try {
        target.invoke()
    } catch (incoming: Throwable) {

        val transformers = listOf(
            HttpErrorTransformer,
            NetworkingErrorTransformer,
            SerializationErrorTransformer
        )

        throw transformers
            .map { it.transform(incoming) }
            .reduce { transformed, another ->
                when {
                    transformed == another -> transformed
                    another == incoming -> transformed
                    else -> another
                }
            }
    }
