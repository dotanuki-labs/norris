package io.dotanuki.norris.networking

import io.dotanuki.burster.using
import io.dotanuki.norris.rest.errors.NetworkingError
import io.dotanuki.norris.rest.errors.NetworkingError.ConnectionSpike
import io.dotanuki.norris.rest.errors.NetworkingError.HostUnreachable
import io.dotanuki.norris.rest.errors.NetworkingError.OperationTimeout
import io.dotanuki.norris.rest.errors.RemoteServiceIntegrationError
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
                values(UnknownHostException("No Internet"), HostUnreachable)
                values(ConnectException(), HostUnreachable)
                values(NoRouteToHostException(), HostUnreachable)
                values(SocketTimeoutException(), OperationTimeout)
                values(IOException("Canceled"), ConnectionSpike)
            }

            thenWith { incoming, expected ->
                assertTransformed(
                    from = incoming,
                    to = expected,
                    using = NetworkingErrorTransformer
                )
            }
        }
    }

    @Test fun `should propagate any other error`() {
        val otherError = RemoteServiceIntegrationError.RemoteSystem

        assertTransformed(
            from = otherError,
            to = otherError,
            using = NetworkingErrorTransformer
        )
    }
}
