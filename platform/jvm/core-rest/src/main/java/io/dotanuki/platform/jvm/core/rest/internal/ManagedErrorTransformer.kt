package io.dotanuki.platform.jvm.core.rest.internal

import io.dotanuki.platform.jvm.core.networking.transformers.DataMarshallingErrorTransformer
import io.dotanuki.platform.jvm.core.networking.transformers.ErrorTransformer
import io.dotanuki.platform.jvm.core.networking.transformers.HttpDrivenErrorTransformer
import io.dotanuki.platform.jvm.core.networking.transformers.NetworkConnectivityErrorTransformer

internal object ManagedErrorTransformer : ErrorTransformer {

    private val transformers = listOf(
        HttpDrivenErrorTransformer,
        NetworkConnectivityErrorTransformer,
        DataMarshallingErrorTransformer
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
