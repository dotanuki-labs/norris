package io.dotanuki.platform.jvm.core.rest.internal.transformers

import io.dotanuki.platform.jvm.core.rest.HttpNetworkingError
import io.dotanuki.platform.jvm.core.rest.internal.NetworkingErrorTransformer
import java.io.IOException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

internal object ConnectivityErrorTransformer : NetworkingErrorTransformer {

    override fun transform(incoming: Throwable) =
        when {
            (!isNetworkingError(incoming)) -> incoming
            isConnectionTimeout(incoming) -> HttpNetworkingError.Connectivity.OperationTimeout
            cannotReachHost(incoming) -> HttpNetworkingError.Connectivity.HostUnreachable
            else -> HttpNetworkingError.Connectivity.ConnectionSpike
        }

    private fun isNetworkingError(error: Throwable) =
        isConnectionTimeout(error) ||
            cannotReachHost(error) ||
            isRequestCanceled(error) ||
            isRequestInterrupted(error)

    private fun isRequestInterrupted(error: Throwable): Boolean =
        error is IOException &&
            error.message?.lowercase()?.contains("unexpected end of stream") ?: false

    private fun isRequestCanceled(error: Throwable) =
        error is IOException &&
            error.message?.lowercase()?.contentEquals("canceled") ?: false

    private fun cannotReachHost(error: Throwable) =
        error is UnknownHostException ||
            error is ConnectException ||
            error is NoRouteToHostException

    private fun isConnectionTimeout(error: Throwable) =
        error is SocketTimeoutException ||
            error is InterruptedIOException && error.message?.lowercase()?.contains("timeout") ?: false
}
