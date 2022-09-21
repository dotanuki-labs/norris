package io.dotanuki.platform.jvm.core.networking.transformers

import io.dotanuki.platform.jvm.core.networking.errors.RemoteServiceIntegrationError.UnexpectedResponse
import kotlinx.serialization.SerializationException

object SerializationErrorTransformer : ErrorTransformer {

    override suspend fun transform(incoming: Throwable) =
        when (incoming) {
            is SerializationException -> UnexpectedResponse
            else -> incoming
        }
}
