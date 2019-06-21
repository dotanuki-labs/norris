package io.dotanuki.norris.networking

import io.dotanuki.norris.rest.errors.RemoteServiceIntegrationError
import kotlinx.serialization.SerializationException
import org.junit.Test

class SerializationErrorTransformerTests {

    @Test fun `should handle serialization errors`() {
        val parseError = SerializationException("Found comments inside this JSON")
        assertTransformed(
            from = parseError,
            to = RemoteServiceIntegrationError.UnexpectedResponse,
            using = SerializationErrorTransformer
        )
    }

    @Test fun `should not handle any other errors`() {
        val toBePropagated = IllegalStateException("Something broke here ...")
        assertTransformed(
            from = toBePropagated,
            to = toBePropagated,
            using = SerializationErrorTransformer
        )
    }
}