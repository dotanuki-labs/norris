package io.dotanuki.norris.networking

import io.dotanuki.norris.rest.errors.ErrorTransformer
import io.dotanuki.norris.rest.errors.RemoteServiceIntegrationError.UnexpectedResponse
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.UnknownFieldException

object SerializationErrorTransformer : ErrorTransformer {

    override suspend fun transform(incoming: Throwable) =
        when (incoming) {
            is MissingFieldException,
            is UnknownFieldException,
            is SerializationException -> UnexpectedResponse
            else -> incoming
        }
}