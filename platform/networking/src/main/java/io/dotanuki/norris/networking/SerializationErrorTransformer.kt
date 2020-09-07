package io.dotanuki.norris.networking

import io.dotanuki.norris.domain.errors.ErrorTransformer
import io.dotanuki.norris.domain.errors.RemoteServiceIntegrationError.UnexpectedResponse
import kotlinx.serialization.SerializationException

object SerializationErrorTransformer : ErrorTransformer {

    override suspend fun transform(incoming: Throwable) =
        when (incoming) {
            is SerializationException -> UnexpectedResponse
            else -> incoming
        }
}