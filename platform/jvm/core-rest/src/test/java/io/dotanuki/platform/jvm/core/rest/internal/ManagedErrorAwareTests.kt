package io.dotanuki.platform.jvm.core.rest.internal

import com.google.common.truth.Truth.assertThat
import io.dotanuki.platform.jvm.core.networking.errors.DataMarshallingError
import io.dotanuki.platform.jvm.core.networking.errors.HttpDrivenError.RemoteSystem
import io.dotanuki.platform.jvm.core.networking.errors.NetworkConnectivityError.HostUnreachable
import java.net.UnknownHostException
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class ManagedErrorAwareTests {

    @Test fun `should transform downstream error with managed execution`() {

        val otherError = IllegalStateException("Houston, we have a problem!")

        listOf(
            UnknownHostException("No Internet") to HostUnreachable,
            SerializationException("Ouch") to DataMarshallingError,
            httpException() to RemoteSystem,
            otherError to otherError
        ).forEach { (incoming, expected) ->
            runBlocking {
                runCatching { ManagedErrorAware { emulateError(incoming) } }
                    .onFailure { assertThat(it).isEqualTo(expected) }
                    .onSuccess { throw AssertionError("Not an error") }
            }
        }
    }

    private suspend fun emulateError(error: Throwable): Unit =
        suspendCoroutine { continuation ->
            continuation.resumeWithException(error)
        }

    private fun httpException(): HttpException {
        val jsonMediaType = "application/json".toMediaTypeOrNull()
        val body = "Server down".toResponseBody(jsonMediaType)
        return HttpException(Response.error<Nothing>(503, body))
    }
}
