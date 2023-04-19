package io.dotanuki.platform.jvm.core.networking.transformers

import io.dotanuki.platform.jvm.core.networking.errors.NetworkConnectivityError
import java.io.IOException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object NetworkConnectivityErrorTransformer : ErrorTransformer {

    override fun transform(incoming: Throwable) =
        when {
            (!isNetworkingError(incoming)) -> incoming
            isConnectionTimeout(incoming) -> NetworkConnectivityError.OperationTimeout
            cannotReachHost(incoming) -> NetworkConnectivityError.HostUnreachable
            else -> NetworkConnectivityError.ConnectionSpike
        }

    private fun isNetworkingError(error: Throwable) =
        isConnectionTimeout(error) ||
            cannotReachHost(error) ||
            isRequestCanceled(error) ||
            isRequestInterrrupted(error)

    private fun isRequestInterrrupted(error: Throwable): Boolean =
        error is IOException &&
            error.message?.contains("unexpected end of stream") ?: false

    private fun isRequestCanceled(error: Throwable) =
        error is IOException &&
            error.message?.contentEquals("Canceled") ?: false

    private fun cannotReachHost(error: Throwable) =
        error is UnknownHostException ||
            error is ConnectException ||
            error is NoRouteToHostException

    private fun isConnectionTimeout(error: Throwable) =
        error is SocketTimeoutException ||
            error is InterruptedIOException && error.message?.contains("timeout") ?: false
}
