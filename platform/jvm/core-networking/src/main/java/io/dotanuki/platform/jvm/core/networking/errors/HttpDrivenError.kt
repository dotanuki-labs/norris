package io.dotanuki.platform.jvm.core.networking.errors

sealed class HttpDrivenError : Throwable() {

    object ClientOrigin : HttpDrivenError()
    object RemoteSystem : HttpDrivenError()

    override fun toString() =
        when (this) {
            ClientOrigin -> "Issue originated from client"
            RemoteSystem -> "Issue incoming from server"
        }
}
