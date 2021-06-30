package io.dotanuki.demos.norris

object RestDataBuilder {

    fun suggestionsPayload(targets: List<String>): String =
        targets.joinToString(prefix = "[", postfix = "]") { "\"$it\"" }.replace(",]", "]")

    fun factsPayload(category: String, fact: String): String =
        FACTS_TEMPLATE
            .replace("<category>", category)
            .replace("<fact>", fact)

    private val FACTS_TEMPLATE =
        """
            {
              "total": 1,
              "result": [
                {
                  "categories": ["<category>"],
                  "icon_url": "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
                  "id": "2wzginmks8azrbaxnamxdw",
                  "url": "https://api.chucknorris.io/jokes/2wzginmks8azrbaxnamxdw",
                  "value": "<fact>"
                }
              ]
            }
        """.trimIndent()
}
