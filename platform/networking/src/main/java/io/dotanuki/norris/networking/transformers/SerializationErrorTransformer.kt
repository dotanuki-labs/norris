package io.dotanuki.norris.networking.transformers

import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError.UnexpectedResponse
import kotlinx.serialization.SerializationException

object SerializationErrorTransformer : ErrorTransformer {

    override suspend fun transform(incoming: Throwable) =
        when (incoming) {
            is SerializationException -> UnexpectedResponse
            else -> incoming
        }
}
