package io.dotanuki.platform.jvm.core.rest.internal

import io.dotanuki.platform.jvm.core.networking.transformers.ErrorTransformer
import io.dotanuki.platform.jvm.core.networking.transformers.HttpErrorTransformer
import io.dotanuki.platform.jvm.core.networking.transformers.NetworkingErrorTransformer
import io.dotanuki.platform.jvm.core.networking.transformers.SerializationErrorTransformer

internal object ManagedErrorTransformer : ErrorTransformer {

    private val transformers = listOf(
        HttpErrorTransformer,
        NetworkingErrorTransformer,
        SerializationErrorTransformer
    )

    override fun transform(incoming: Throwable): Throwable =
        transformers
            .map { it.transform(incoming) }
            .reduce { transformed, another ->
                when {
                    transformed == another -> transformed
                    another == incoming -> transformed
                    else -> another
                }
            }
}
