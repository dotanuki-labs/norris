package io.dotanuki.platform.jvm.core.rest

sealed class HttpNetworkingError : Throwable() {

    sealed class Connectivity : HttpNetworkingError() {

        object HostUnreachable : Connectivity()
        object OperationTimeout : Connectivity()
        object ConnectionSpike : Connectivity()

        override fun toString() =
            when (this) {
                HostUnreachable -> "Cannot reach remote host"
                OperationTimeout -> "Network operation timed out"
                ConnectionSpike -> "In-flight network operation interrupted"
            }
    }

    sealed class Restful : HttpNetworkingError() {
        data class Client(val status: Int) : Restful()
        data class Server(val status: Int) : Restful()

        override fun toString() =
            when (this) {
                is Client -> "Request was rejected by the server (status = $status)"
                is Server -> "Remote server seems unhealthy (status = $status)"
            }
    }

    // Note this could also be a broken contract, but we don't do a differentation
    object DataMarshallingError : HttpNetworkingError() {
        override fun toString() = "Error when marshalling data from HTTP request"
    }
}
