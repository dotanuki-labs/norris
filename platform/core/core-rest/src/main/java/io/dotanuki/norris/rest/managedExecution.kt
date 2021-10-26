package io.dotanuki.norris.rest

import io.dotanuki.norris.networking.transformers.HttpErrorTransformer
import io.dotanuki.norris.networking.transformers.NetworkingErrorTransformer
import io.dotanuki.norris.networking.transformers.SerializationErrorTransformer

private val transformers = listOf(
    HttpErrorTransformer,
    NetworkingErrorTransformer,
    SerializationErrorTransformer
)

suspend fun <T> managedExecution(target: suspend () -> T): T =
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
