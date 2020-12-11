package io.dotanuki.norris.networking

import io.dotanuki.norris.domain.errors.ErrorTransformer
import io.dotanuki.norris.domain.errors.RemoteServiceIntegrationError
import retrofit2.HttpException

object HttpIntegrationErrorTransformer : ErrorTransformer {

    override suspend fun transform(incoming: Throwable) =
        when (incoming) {
            is HttpException -> translateUsingStatusCode(incoming.code())
            else -> incoming
        }

    private fun translateUsingStatusCode(code: Int) =
        when (code) {
            in 400..499 -> RemoteServiceIntegrationError.ClientOrigin
            else -> RemoteServiceIntegrationError.RemoteSystem
        }
}
