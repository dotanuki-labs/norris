package io.dotanuki.platform.jvm.core.rest.internal.transformers

import io.dotanuki.platform.jvm.core.rest.HttpNetworkingError
import io.dotanuki.platform.jvm.core.rest.internal.NetworkingErrorTransformer
import kotlinx.serialization.SerializationException

internal object DataMarshallingErrorTransformer : NetworkingErrorTransformer {

    override fun transform(incoming: Throwable) =
        when (incoming) {
            is SerializationException -> HttpNetworkingError.DataMarshallingError
            else -> incoming
        }
}
