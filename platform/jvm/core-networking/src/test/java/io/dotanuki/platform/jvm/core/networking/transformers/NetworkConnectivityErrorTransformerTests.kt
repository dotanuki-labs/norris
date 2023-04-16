package io.dotanuki.platform.jvm.core.networking.transformers

import com.google.common.truth.Truth.assertThat
import io.dotanuki.platform.jvm.core.networking.transformers.CheckErrorTransformation.Companion.checkTransformation
import io.dotanuki.platform.jvm.core.networking.errors.NetworkConnectivityError
import io.dotanuki.platform.jvm.core.networking.errors.HttpDrivenError
import org.junit.Test
import java.io.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkConnectivityErrorTransformerTests {

    @Test fun `should transform networking error from downstream`() {
        listOf(
            UnknownHostException("No Internet") to NetworkConnectivityError.HostUnreachable,
            ConnectException() to NetworkConnectivityError.HostUnreachable,
            NoRouteToHostException() to NetworkConnectivityError.HostUnreachable,
            SocketTimeoutException() to NetworkConnectivityError.OperationTimeout,
            IOException("Canceled") to NetworkConnectivityError.ConnectionSpike
        ).forEach { (thrown, expected) ->
            assertTransformation(thrown, expected)
        }
    }

    @Test fun `should propagate any other error`() {
        val otherError = HttpDrivenError.RemoteSystem
        assertTransformation(otherError, otherError)
    }

    private fun assertTransformation(target: Throwable, expected: Throwable) {
        checkTransformation(
            from = target,
            using = NetworkConnectivityErrorTransformer,
            check = { transformed -> assertThat(transformed).isEqualTo(expected) }
        )
    }
}
