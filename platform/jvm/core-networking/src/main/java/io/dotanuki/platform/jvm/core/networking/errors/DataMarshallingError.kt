package io.dotanuki.platform.jvm.core.networking.errors

object DataMarshallingError : Throwable() {
    override fun toString() = "Error when marshalling/unmarshalling data from HTTP request"
}
