package io.dotanuki.platform.jvm.core.rest.internal.transformers

import com.google.common.truth.Truth.assertThat
import io.dotanuki.platform.jvm.core.rest.HttpNetworkingError
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class RestfulErrorTransformerTests {
    @Test fun `should transform http error from downstream`() {
        listOf(
            httpException<Any>(418, "teapot") to HttpNetworkingError.Restful.Client(418),
            httpException<Any>(503, "Internal Server Error") to HttpNetworkingError.Restful.Server(503)
        ).forEach { (incoming, expected) ->
            val transformed = RestfulErrorTransformer.transform(incoming)
            assertThat(transformed).isEqualTo(expected)
        }
    }

    @Test fun `should propagate any other error`() {
        val otherError = IllegalStateException("Houston, we have a problem!")
        val transformed = RestfulErrorTransformer.transform(otherError)
        assertThat(transformed).isEqualTo(otherError)
    }

    private fun <T> httpException(statusCode: Int, errorMessage: String): HttpException {
        val jsonMediaType = "application/json".toMediaTypeOrNull()
        val body = errorMessage.toResponseBody(jsonMediaType)
        return HttpException(Response.error<T>(statusCode, body))
    }
}
