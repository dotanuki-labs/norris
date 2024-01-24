package io.dotanuki.features.facts.presentation

import io.dotanuki.features.facts.domain.ChuckNorrisFact

data class FactsPresentation(
    val relatedQuery: String,
    val facts: List<FactDisplayRow>,
)

data class FactDisplayRow(
    val url: String,
    val fact: String,
    val displayWithSmallerFontSize: Boolean,
) {
    companion object {
        private const val SMALL_FACT_LIMIT = 100

        operator fun invoke(fact: ChuckNorrisFact) =
            with(fact) {
                FactDisplayRow(
                    url = shareableUrl,
                    fact = textual,
                    displayWithSmallerFontSize = textual.length >= SMALL_FACT_LIMIT
                )
            }
    }
}
