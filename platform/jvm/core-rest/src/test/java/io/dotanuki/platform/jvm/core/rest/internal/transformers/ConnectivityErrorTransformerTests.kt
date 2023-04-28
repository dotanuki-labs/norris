package io.dotanuki.platform.jvm.core.rest.internal.transformers

import com.google.common.truth.Truth.assertThat
import io.dotanuki.platform.jvm.core.rest.HttpNetworkingError.Connectivity
import java.io.IOException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import org.junit.Test

class ConnectivityErrorTransformerTests {

    @Test fun `should transform connectivity errors surfaced by the HTTP stack`() {
        listOf(
            UnknownHostException("No Internet") to Connectivity.HostUnreachable,
            ConnectException() to Connectivity.HostUnreachable,
            NoRouteToHostException() to Connectivity.HostUnreachable,
            SocketTimeoutException() to Connectivity.OperationTimeout,
            InterruptedIOException("operation timeout") to Connectivity.OperationTimeout,
            IOException("Canceled") to Connectivity.ConnectionSpike,
            IOException("unexpected end of stream") to Connectivity.ConnectionSpike
        ).forEach { (thrown, expected) ->
            val transformed = ConnectivityErrorTransformer.transform(thrown)
            assertThat(transformed).isEqualTo(expected)
        }
    }

    @Test fun `should propagate any other error`() {
        val unrelatedError = IllegalStateException("Something went wrong")
        val transformed = ConnectivityErrorTransformer.transform(unrelatedError)
        assertThat(transformed).isEqualTo(unrelatedError)
    }
}
