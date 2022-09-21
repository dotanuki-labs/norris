package io.dotanuki.norris.features.search

import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.features.search.domain.SearchQueryValidation
import org.junit.Test

class SearchQueryValidationTests {

    @Test fun `should validate query`() {
        listOf(
            "" to false,
            "a" to false,
            "u2" to false,
            "11" to false,
            "dev" to true,
            "code" to true,
            "code more" to false,
            "thequerytermshouldbesmallerthanthis" to false
        ).forEach { (query, validation) ->
            assertThat(SearchQueryValidation.validate(query)).isEqualTo(validation)
        }
    }
}
