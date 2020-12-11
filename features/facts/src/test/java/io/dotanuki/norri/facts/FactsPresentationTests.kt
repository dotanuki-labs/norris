package io.dotanuki.norri.facts

import io.dotanuki.norris.domain.model.ChuckNorrisFact
import io.dotanuki.norris.domain.model.RelatedCategory
import io.dotanuki.norris.facts.FactDisplayRow
import io.dotanuki.norris.facts.FactsPresentation
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class FactsPresentationTests {

    @Test fun `should map facts presentation`() {
        val facts = listOf(
            ChuckNorrisFact(
                id = "lhan43nqsgowtaffzxouua",
                shareableUrl = "https://api.chucknorris.io/jokes/lhan43nqsgowtaffzxouua",
                textual = "Chuck Norris divides by zero.",
                category = RelatedCategory.Available("math")
            ),
            ChuckNorrisFact(
                id = "2wzginmks8azrbaxnamxdw",
                shareableUrl = "https://api.chucknorris.io/jokes/2wzginmks8azrbaxnamxdw",
                textual = "As 7 maravilhas do mundo moderno são: a mão direita e esquerda de Chuck Norris, seu pé direito e esquerdo, seu cinturão, seu chapéu e sua barba",
                category = RelatedCategory.Missing
            )
        )

        val expected = FactsPresentation(
            "god like",
            listOf(
                FactDisplayRow(
                    tag = RelatedCategory.Available("math"),
                    url = "https://api.chucknorris.io/jokes/lhan43nqsgowtaffzxouua",
                    fact = "Chuck Norris divides by zero.",
                    displayWithSmallerFontSize = false
                ),
                FactDisplayRow(
                    tag = RelatedCategory.Missing,
                    url = "https://api.chucknorris.io/jokes/2wzginmks8azrbaxnamxdw",
                    fact = "As 7 maravilhas do mundo moderno são: a mão direita e esquerda de Chuck Norris, seu pé direito e esquerdo, seu cinturão, seu chapéu e sua barba",
                    displayWithSmallerFontSize = true
                )
            )
        )

        val presentation = FactsPresentation("god like", facts.map { FactDisplayRow(it) })
        assertThat(expected).isEqualTo(presentation)
    }
}
