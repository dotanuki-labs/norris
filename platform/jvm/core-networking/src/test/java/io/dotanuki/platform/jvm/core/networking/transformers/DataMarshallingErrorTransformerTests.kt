package io.dotanuki.platform.jvm.core.networking.transformers

import com.google.common.truth.Truth.assertThat
import io.dotanuki.platform.jvm.core.networking.errors.DataMarshallingError
import io.dotanuki.platform.jvm.core.networking.transformers.CheckErrorTransformation.Companion.checkTransformation
import kotlinx.serialization.SerializationException
import org.junit.Test

class DataMarshallingErrorTransformerTests {

    @Test fun `should transform serialization error from downstream`() {
        val parseError = SerializationException("Found comments inside this JSON")
        val expected = DataMarshallingError
        assertTransformation(parseError, expected)
    }

    @Test fun `should not handle any other errors`() {
        val otherError = IllegalStateException("Something broke here ...")
        assertTransformation(otherError, otherError)
    }

    private fun assertTransformation(target: Throwable, expected: Throwable) {
        checkTransformation(
            from = target,
            using = DataMarshallingErrorTransformer,
            check = { transformed -> assertThat(transformed).isEqualTo(expected) }
        )
    }
}
