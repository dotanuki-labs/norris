package io.dotanuki.norris.domain

import com.google.common.truth.Truth.assertThat
import io.dotanuki.burster.using
import org.junit.Test

class SearchQueryValidationTests {

    @Test fun `should validate query`() {

        using<String, Boolean> {

            burst {
                values("", false)
                values("a", false)
                values("u2", false)
                values("11", false)
                values("dev", true)
                values("code", true)
                values("code more", false)
                values("thequerytermshouldbesmallerthanthis", false)
            }

            thenWith { query, validation ->
                assertThat(SearchQueryValidation.validate(query)).isEqualTo(validation)
            }
        }
    }
}
