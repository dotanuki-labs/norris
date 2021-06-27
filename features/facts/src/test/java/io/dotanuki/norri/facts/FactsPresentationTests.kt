package io.dotanuki.norri.facts

import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.facts.domain.ChuckNorrisFact
import io.dotanuki.norris.facts.presentation.FactDisplayRow
import io.dotanuki.norris.facts.presentation.FactsPresentation
import org.junit.Test

class FactsPresentationTests {

    @Test fun `should map facts presentation`() {
        val facts = listOf(
            ChuckNorrisFact(
                id = "lhan43nqsgowtaffzxouua",
                shareableUrl = "https://api.chucknorris.io/jokes/lhan43nqsgowtaffzxouua",
                textual = "Chuck Norris divides by zero.",
            ),
            ChuckNorrisFact(
                id = "2wzginmks8azrbaxnamxdw",
                shareableUrl = "https://api.chucknorris.io/jokes/2wzginmks8azrbaxnamxdw",
                textual = "As 7 maravilhas do mundo moderno são: a mão direita e esquerda de Chuck Norris, seu pé direito e esquerdo, seu cinturão, seu chapéu e sua barba",
            )
        )

        val expected = FactsPresentation(
            "god like",
            listOf(
                FactDisplayRow(
                    url = "https://api.chucknorris.io/jokes/lhan43nqsgowtaffzxouua",
                    fact = "Chuck Norris divides by zero.",
                    displayWithSmallerFontSize = false
                ),
                FactDisplayRow(
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
