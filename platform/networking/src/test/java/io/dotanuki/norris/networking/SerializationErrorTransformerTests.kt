package io.dotanuki.norris.networking

import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.networking.CheckErrorTransformation.Companion.checkTransformation
import kotlinx.serialization.SerializationException
import org.junit.Test

class SerializationErrorTransformerTests {

    @Test fun `should handle serialization errors`() {
        val parseError = SerializationException("Found comments inside this JSON")
        val expected = RemoteServiceIntegrationError.UnexpectedResponse

        checkTransformation(
            from = parseError,
            using = SerializationErrorTransformer,
            check = { transformed -> assertThat(transformed).isEqualTo(expected) }
        )
    }

    @Test fun `should not handle any other errors`() {
        val toBePropagated = IllegalStateException("Something broke here ...")
        checkTransformation(
            from = toBePropagated,
            using = SerializationErrorTransformer,
            check = { transformed -> assertThat(transformed).isEqualTo(toBePropagated) }
        )
    }
}
