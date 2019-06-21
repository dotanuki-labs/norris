package io.dotanuki.norris.rest.errors

sealed class RemoteServiceIntegrationError : Throwable() {

    object ClientOrigin : RemoteServiceIntegrationError()
    object RemoteSystem : RemoteServiceIntegrationError()
    object UnexpectedResponse : RemoteServiceIntegrationError()

    override fun toString() =
        when (this) {
            ClientOrigin -> "Issue originated from client"
            RemoteSystem -> "Issue incoming from server"
            UnexpectedResponse -> "Broken contract"
        }
}