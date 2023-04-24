package io.dotanuki.platform.jvm.testing.rest

import io.dotanuki.platform.jvm.core.rest.RawFact
import io.dotanuki.platform.jvm.core.rest.RawSearch

object RestDataBuilder {

    fun rawSearch(id: String, fact: String) = RawSearch(
        listOf(
            RawFact(id, "https://api.chucknorris.io/jokes/$id", fact, emptyList())
        )
    )

    fun suggestionsPayload(targets: List<String>): String =
        targets.joinToString(prefix = "[", postfix = "]") { "\"$it\"" }.replace(",]", "]")

}
