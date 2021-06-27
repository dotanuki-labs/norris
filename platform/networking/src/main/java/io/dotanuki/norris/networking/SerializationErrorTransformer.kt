package io.dotanuki.norris.networking

import io.dotanuki.norris.networking.RemoteServiceIntegrationError.UnexpectedResponse
import kotlinx.serialization.SerializationException

object SerializationErrorTransformer : ErrorTransformer {

    override suspend fun transform(incoming: Throwable) =
        when (incoming) {
            is SerializationException -> UnexpectedResponse
            else -> incoming
        }
}
