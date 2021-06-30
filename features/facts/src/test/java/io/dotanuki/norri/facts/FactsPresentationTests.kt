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
                textual = "Null pointer crashes with ChuckNorrisException",
            )
        )

        val expected = FactsPresentation(
            "humor",
            listOf(
                FactDisplayRow(
                    url = "https://api.chucknorris.io/jokes/lhan43nqsgowtaffzxouua",
                    fact = "Chuck Norris divides by zero.",
                    displayWithSmallerFontSize = false
                ),
                FactDisplayRow(
                    url = "https://api.chucknorris.io/jokes/2wzginmks8azrbaxnamxdw",
                    fact = "Null pointer crashes with ChuckNorrisException",
                    displayWithSmallerFontSize = true
                )
            )
        )

        val presentation = FactsPresentation("humor", facts.map { FactDisplayRow(it) })
        assertThat(expected).isEqualTo(presentation)
    }
}
