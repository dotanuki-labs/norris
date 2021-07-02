package io.dotanuki.norris.networking

import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.networking.CheckErrorTransformation.Companion.checkTransformation
import io.dotanuki.norris.networking.errors.NetworkingError
import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError
import io.dotanuki.norris.networking.transformers.NetworkingErrorTransformer
import org.junit.Test
import java.io.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkingErrorTransformerTests {

    @Test fun `should transform networking error from downstream`() {
        listOf(
            UnknownHostException("No Internet") to NetworkingError.HostUnreachable,
            ConnectException() to NetworkingError.HostUnreachable,
            NoRouteToHostException() to NetworkingError.HostUnreachable,
            SocketTimeoutException() to NetworkingError.OperationTimeout,
            IOException("Canceled") to NetworkingError.ConnectionSpike
        ).forEach { (thrown, expected) ->
            assertTransformation(thrown, expected)
        }
    }

    @Test fun `should propagate any other error`() {
        val otherError = RemoteServiceIntegrationError.RemoteSystem
        assertTransformation(otherError, otherError)
    }

    private fun assertTransformation(target: Throwable, expected: Throwable) {
        checkTransformation(
            from = target,
            using = NetworkingErrorTransformer,
            check = { transformed -> assertThat(transformed).isEqualTo(expected) }
        )
    }
}
