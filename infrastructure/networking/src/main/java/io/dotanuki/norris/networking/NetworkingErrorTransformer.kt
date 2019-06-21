package io.dotanuki.norris.networking

import io.dotanuki.norris.rest.errors.ErrorTransformer
import io.dotanuki.norris.rest.errors.NetworkingError.ConnectionSpike
import io.dotanuki.norris.rest.errors.NetworkingError.HostUnreachable
import io.dotanuki.norris.rest.errors.NetworkingError.OperationTimeout
import java.io.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object NetworkingErrorTransformer : ErrorTransformer {

    override suspend fun transform(incoming: Throwable) =
        when {
            (!isNetworkingError(incoming)) -> incoming
            isConnectionTimeout(incoming) -> OperationTimeout
            cannotReachHost(incoming) -> HostUnreachable
            else -> ConnectionSpike
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