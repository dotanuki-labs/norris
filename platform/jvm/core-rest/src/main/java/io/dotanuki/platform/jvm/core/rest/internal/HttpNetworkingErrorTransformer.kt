package io.dotanuki.platform.jvm.core.rest.internal

import io.dotanuki.platform.jvm.core.rest.internal.transformers.ConnectivityErrorTransformer
import io.dotanuki.platform.jvm.core.rest.internal.transformers.DataMarshallingErrorTransformer
import io.dotanuki.platform.jvm.core.rest.internal.transformers.RestfulErrorTransformer

internal object HttpNetworkingErrorTransformer : NetworkingErrorTransformer {

    private val transformers = listOf(
        RestfulErrorTransformer,
        ConnectivityErrorTransformer,
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
