package io.dotanuki.platform.jvm.core.networking.transformers

import io.dotanuki.platform.jvm.core.networking.errors.DataMarshallingError
import kotlinx.serialization.SerializationException

object DataMarshallingErrorTransformer : ErrorTransformer {

    override fun transform(incoming: Throwable) =
        when (incoming) {
            is SerializationException -> DataMarshallingError
            else -> incoming
        }
}
