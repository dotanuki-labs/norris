package io.dotanuki.features.facts.data

import com.google.common.truth.Truth.assertThat
import io.dotanuki.features.facts.domain.ChuckNorrisFact
import io.dotanuki.platform.jvm.core.networking.errors.HttpDrivenError
import io.dotanuki.platform.jvm.core.rest.RawSearch
import io.dotanuki.platform.jvm.testing.rest.RestDataBuilder
import io.dotanuki.platform.jvm.testing.rest.RestScenario
import io.dotanuki.platform.jvm.testing.rest.RestTestHelper
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class FactsDataSourceTests {

    private val restTestHelper = RestTestHelper()
    private val dataSource = FactsDataSource(restTestHelper.createClient())

    @Test fun `should handle no results properly`() {
        val scenario = RestScenario.Facts(RawSearch(emptyList()))
        restTestHelper.defineScenario(scenario)

        val noFacts = emptyList<ChuckNorrisFact>()
        assertThat(simpleSearch()).isEqualTo(noFacts)
    }

    @Test fun `should handle downstream error`() {
        val incomingError = HttpDrivenError.RemoteSystem
        val scenario = RestScenario.Error(incomingError)
        restTestHelper.defineScenario(scenario)

        runCatching { simpleSearch() }
            .onFailure { assertThat(it).isEqualTo(incomingError) }
            .onSuccess { throw AssertionError("Not a failure") }
    }

    @Test fun `should fetch facts with valid query term`() {

        val facts = RestDataBuilder.rawSearch(
            "lhan43nqsgowtaffzxouua" to "Police label anyone attacking Chuck Norris as a Code 45-11.... A suicide.",
            "2wzginmks8azrbaxnamxdw" to "SQL statements that Chuck Norris code have implicit COMMITs in its end."
        )

        restTestHelper.defineScenario(RestScenario.Facts(facts))

        val expected = listOf(
            ChuckNorrisFact(
                id = "lhan43nqsgowtaffzxouua",
                shareableUrl = "https://api.chucknorris.io/jokes/lhan43nqsgowtaffzxouua",
                textual = "Police label anyone attacking Chuck Norris as a Code 45-11.... A suicide."
            ),
            ChuckNorrisFact(
                id = "2wzginmks8azrbaxnamxdw",
                shareableUrl = "https://api.chucknorris.io/jokes/2wzginmks8azrbaxnamxdw",
                textual = "SQL statements that Chuck Norris code have implicit COMMITs in its end."
            )
        )

        assertThat(simpleSearch()).isEqualTo(expected)
    }

    private fun simpleSearch(): List<ChuckNorrisFact> = runBlocking {
        dataSource.search("Norris")
    }
}
