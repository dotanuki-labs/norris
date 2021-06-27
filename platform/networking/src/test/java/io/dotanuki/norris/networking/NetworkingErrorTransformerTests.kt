package io.dotanuki.norris.networking

import com.google.common.truth.Truth.assertThat
import io.dotanuki.burster.using
import io.dotanuki.norris.networking.CheckErrorTransformation.Companion.checkTransformation
import org.junit.Test
import java.io.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkingErrorTransformerTests {

    @Test fun `should transform proper throwable into networking error`() {
        using<Throwable, NetworkingError> {

            burst {
                values(UnknownHostException("No Internet"), NetworkingError.HostUnreachable)
                values(ConnectException(), NetworkingError.HostUnreachable)
                values(NoRouteToHostException(), NetworkingError.HostUnreachable)
                values(SocketTimeoutException(), NetworkingError.OperationTimeout)
                values(IOException("Canceled"), NetworkingError.ConnectionSpike)
            }

            thenWith { incoming, expected ->
                checkTransformation(
                    from = incoming,
                    using = NetworkingErrorTransformer,
                    check = { transformed -> assertThat(transformed).isEqualTo(expected) }
                )
            }
        }
    }

    @Test fun `should propagate any other error`() {
        val otherError = RemoteServiceIntegrationError.RemoteSystem

        checkTransformation(
            from = otherError,
            using = NetworkingErrorTransformer,
            check = { transformed -> assertThat(transformed).isEqualTo(otherError) }
        )
    }
}
