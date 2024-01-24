package io.dotanuki.platform.jvm.core.rest.internal.transformers

import io.dotanuki.platform.jvm.core.rest.HttpNetworkingError
import io.dotanuki.platform.jvm.core.rest.internal.NetworkingErrorTransformer
import retrofit2.HttpException

internal object RestfulErrorTransformer : NetworkingErrorTransformer {
    override fun transform(incoming: Throwable) =
        when (incoming) {
            is HttpException -> translateUsingStatusCode(incoming.code())
            else -> incoming
        }

    private fun translateUsingStatusCode(code: Int) =
        when (code) {
            in 400..499 -> HttpNetworkingError.Restful.Client(code)
            else -> HttpNetworkingError.Restful.Server(code)
        }
}
