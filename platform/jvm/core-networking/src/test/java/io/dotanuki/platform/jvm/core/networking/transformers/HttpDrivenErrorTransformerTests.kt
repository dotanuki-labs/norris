package io.dotanuki.platform.jvm.core.networking.transformers

import com.google.common.truth.Truth.assertThat
import io.dotanuki.platform.jvm.core.networking.errors.HttpDrivenError.ClientOrigin
import io.dotanuki.platform.jvm.core.networking.errors.HttpDrivenError.RemoteSystem
import io.dotanuki.platform.jvm.core.networking.transformers.CheckErrorTransformation.Companion.checkTransformation
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class HttpDrivenErrorTransformerTests {

    @Test fun `should transform http error from downstream`() {
        listOf(
            httpException<Any>(418, "teapot") to ClientOrigin,
            httpException<Any>(503, "Internal Server Error") to RemoteSystem
        ).forEach { (incoming, expected) ->
            assertTransformation(incoming, expected)
        }
    }

    @Test fun `should propagate any other error`() {
        val otherError = IllegalStateException("Houston, we have a problem!")
        assertTransformation(otherError, otherError)
    }

    private fun assertTransformation(target: Throwable, expected: Throwable) {
        checkTransformation(
            from = target,
            using = HttpDrivenErrorTransformer,
            check = { transformed -> assertThat(transformed).isEqualTo(expected) }
        )
    }

    private fun <T> httpException(statusCode: Int, errorMessage: String): HttpException {
        val jsonMediaType = "application/json".toMediaTypeOrNull()
        val body = errorMessage.toResponseBody(jsonMediaType)
        return HttpException(Response.error<T>(statusCode, body))
    }
}