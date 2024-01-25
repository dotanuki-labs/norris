package io.dotanuki.platform.jvm.core.rest.internal.transformers

import com.google.common.truth.Truth.assertThat
import io.dotanuki.platform.jvm.core.rest.HttpNetworkingError
import kotlinx.serialization.SerializationException
import org.junit.Test

class DataMarshallingErrorTransformerTests {
    @Test fun `should transform serialization-related errors`() {
        val parseError = SerializationException("Found comments inside this JSON")

        val transformed = DataMarshallingErrorTransformer.transform(parseError)
        val expected = HttpNetworkingError.DataMarshallingError
        assertThat(transformed).isEqualTo(expected)
    }

    @Test fun `should not handle any other errors`() {
        val otherError = IllegalStateException("Something broke here ...")
        val transformed = DataMarshallingErrorTransformer.transform(otherError)
        assertThat(transformed).isEqualTo(otherError)
    }
}
