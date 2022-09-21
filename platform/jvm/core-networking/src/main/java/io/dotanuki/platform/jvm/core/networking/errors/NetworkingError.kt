package io.dotanuki.platform.jvm.core.networking.errors

sealed class NetworkingError : Throwable() {

    object HostUnreachable : NetworkingError()
    object OperationTimeout : NetworkingError()
    object ConnectionSpike : NetworkingError()

    override fun toString() =
        when (this) {
            HostUnreachable -> "Cannot reach remote host"
            OperationTimeout -> "Networking operation timed out"
            ConnectionSpike -> "In-flight networking operation interrupted"
        }
}
