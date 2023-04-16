package io.dotanuki.platform.jvm.core.networking.transformers

import io.dotanuki.platform.jvm.core.networking.errors.NetworkConnectivityError
import java.io.IOException
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
            isRequestCanceled(error)

    private fun isRequestCanceled(throwable: Throwable) =
        throwable is IOException &&
            throwable.message?.contentEquals("Canceled") ?: false

    private fun cannotReachHost(error: Throwable) =
        error is UnknownHostException ||
            error is ConnectException ||
            error is NoRouteToHostException

    private fun isConnectionTimeout(error: Throwable) =
        error is SocketTimeoutException
}
