package io.dotanuki.norris.facts

import io.dotanuki.norris.domain.model.ChuckNorrisFact
import io.dotanuki.norris.domain.model.RelatedCategory

data class FactsPresentation(
    val facts: List<FactDisplayRow>
)

data class FactDisplayRow(
    val tag: RelatedCategory,
    val url: String,
    val fact: String,
    val displayWithSmallerFontSize: Boolean
) {

    companion object {

        private const val SMALL_FACT_LIMIT = 100

        operator fun invoke(fact: ChuckNorrisFact) = with(fact) {
            FactDisplayRow(
                tag = category,
                url = shareableUrl,
                fact = textual,
                displayWithSmallerFontSize = textual.length >= SMALL_FACT_LIMIT
            )
        }
    }
}