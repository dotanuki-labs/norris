package io.dotanuki.platform.jvm.core.networking.errors

sealed class NetworkConnectivityError : Throwable() {

    object HostUnreachable : NetworkConnectivityError()
    object OperationTimeout : NetworkConnectivityError()
    object ConnectionSpike : NetworkConnectivityError()

    override fun toString() =
        when (this) {
            HostUnreachable -> "Cannot reach remote host"
            OperationTimeout -> "Networking operation timed out"
            ConnectionSpike -> "In-flight networking operation interrupted"
        }
}
