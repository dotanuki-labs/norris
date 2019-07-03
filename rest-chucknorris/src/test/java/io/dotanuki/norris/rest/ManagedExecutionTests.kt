package io.dotanuki.norris.rest

import io.dotanuki.burster.using
import io.dotanuki.norris.domain.errors.NetworkingError.HostUnreachable
import io.dotanuki.norris.domain.errors.RemoteServiceIntegrationError.RemoteSystem
import io.dotanuki.norris.domain.errors.RemoteServiceIntegrationError.UnexpectedResponse
import io.dotanuki.norris.domain.rest.managedExecution
import io.dotanuki.norris.rest.util.unwrapCaughtError
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class ManagedExecutionTests {

    @Test fun `should transform error with managed execution`() {

        val toBePropagated = IllegalStateException("Houston, we have a problem!")

        using<Throwable, Throwable> {

            burst {
                values(UnknownHostException("No Internet"), HostUnreachable)
                values(SerializationException("Ouch"), UnexpectedResponse)
                values(httpException(), RemoteSystem)
                values(toBePropagated, toBePropagated)
            }

            thenWith { incoming, expected ->
                runBlocking {
                    val result = runCatching { managedExecution { emulateError(incoming) } }
                    val unwrapped = unwrapCaughtError(result)
                    assertThat(unwrapped).isEqualTo(expected)
                }
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