package io.dotanuki.platform.jvm.testing.rest

import java.util.UUID

object RestDataBuilder {

    fun suggestionsPayload(targets: List<String>): String =
        targets.joinToString(prefix = "[", postfix = "]") { "\"$it\"" }.replace(",]", "]")

    fun factsPayload(category: String, fact: String): String =
        FACTS_TEMPLATE
            .replace("<category>", category)
            .replace("<fact>", fact)

    val FACT_ID = UUID.randomUUID().toString()
    val FACT_URL = "https://api.chucknorris.io/jokes/$FACT_ID"

    private val FACTS_TEMPLATE =
        """
            {
              "total": 1,
              "result": [
                {
                  "categories": ["<category>"],
                  "icon_url": "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
                  "id": "$FACT_ID",
                  "url": "$FACT_URL",
                  "value": "<fact>"
                }
              ]
            }
        """.trimIndent()
}
