package io.dotanuki.platform.jvm.core.networking.transformers

import io.dotanuki.platform.jvm.core.networking.errors.HttpDrivenError
import retrofit2.HttpException

object HttpDrivenErrorTransformer : ErrorTransformer {

    override fun transform(incoming: Throwable) =
        when (incoming) {
            is HttpException -> translateUsingStatusCode(incoming.code())
            else -> incoming
        }

    private fun translateUsingStatusCode(code: Int) =
        when (code) {
            in 400..499 -> HttpDrivenError.ClientOrigin
            else -> HttpDrivenError.RemoteSystem
        }
}
