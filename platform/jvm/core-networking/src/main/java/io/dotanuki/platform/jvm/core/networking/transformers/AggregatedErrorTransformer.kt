package io.dotanuki.platform.jvm.core.networking.transformers

object AggregatedErrorTransformer {

    val transformers = listOf(
        HttpErrorTransformer,
        NetworkingErrorTransformer,
        SerializationErrorTransformer
    )

    fun transform(incoming: Throwable): Throwable =
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
