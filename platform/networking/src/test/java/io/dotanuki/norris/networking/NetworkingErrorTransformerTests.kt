package io.dotanuki.norris.networking

import io.dotanuki.burster.using
import io.dotanuki.norris.domain.errors.NetworkingError
import io.dotanuki.norris.domain.errors.NetworkingError.ConnectionSpike
import io.dotanuki.norris.domain.errors.NetworkingError.HostUnreachable
import io.dotanuki.norris.domain.errors.NetworkingError.OperationTimeout
import io.dotanuki.norris.domain.errors.RemoteServiceIntegrationError
import io.dotanuki.norris.networking.CheckErrorTransformation.Companion.checkTransformation
import org.assertj.core.api.Assertions.assertThat
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
