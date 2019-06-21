package io.dotanuki.norris.rest

import io.dotanuki.norris.networking.HttpIntegrationErrorTransformer
import io.dotanuki.norris.networking.NetworkingErrorTransformer
import io.dotanuki.norris.networking.SerializationErrorTransformer

private val transformers = listOf(
    HttpIntegrationErrorTransformer,
    NetworkingErrorTransformer,
    SerializationErrorTransformer
)

internal suspend fun <T> managedExecution(target: suspend () -> T): T =
    try {
        target.invoke()
    } catch (incoming: Throwable) {
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

