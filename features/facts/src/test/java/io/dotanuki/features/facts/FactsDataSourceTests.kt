package io.dotanuki.features.facts

import com.google.common.truth.Truth.assertThat
import io.dotanuki.features.facts.data.FactsDataSource
import io.dotanuki.features.facts.domain.ChuckNorrisFact
import io.dotanuki.platform.jvm.core.networking.errors.RemoteServiceIntegrationError
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient
import io.dotanuki.platform.jvm.core.rest.RawFact
import io.dotanuki.platform.jvm.core.rest.RawSearch
import io.dotanuki.platform.jvm.testing.rest.FakeChuckNorrisService
import io.dotanuki.platform.jvm.testing.rest.FakeChuckNorrisService.Scenario
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class FactsDataSourceTests {

    private val service = FakeChuckNorrisService()
    private val dataSource = FactsDataSource(
        ChuckNorrisServiceClient(service)
    )

    @Test fun `should handle no results properly`() {

        service.scenario = Scenario.FactsWithSuccess(
            RawSearch(emptyList())
        )

        val noFacts = emptyList<ChuckNorrisFact>()
        assertThat(simpleSearch()).isEqualTo(noFacts)
    }

    @Test fun `should handle downstream error`() {

        val incomingError = RemoteServiceIntegrationError.RemoteSystem

        service.scenario = Scenario.FactsWithError(incomingError)

        runCatching { simpleSearch() }
            .onFailure { assertThat(it).isEqualTo(incomingError) }
            .onSuccess { throw AssertionError("Not a failure") }
    }

    @Test fun `should fetch facts with valid query term`() {

        service.scenario = Scenario.FactsWithSuccess(
            RawSearch(
                listOf(
                    RawFact(
                        id = "lhan43nqsgowtaffzxouua",
                        url = "https://api.chucknorris.io/jokes/lhan43nqsgowtaffzxouua",
                        value = "Police label anyone attacking Chuck Norris as a Code 45-11.... A suicide.",
                        categories = listOf("random")
                    ),
                    RawFact(
                        id = "2wzginmks8azrbaxnamxdw",
                        url = "https://api.chucknorris.io/jokes/2wzginmks8azrbaxnamxdw",
                        value = "SQL statements that Chuck Norris code have implicit COMMITs in its end.",
                        categories = emptyList()
                    )
                )
            )
        )

        val facts = listOf(
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

        assertThat(simpleSearch()).isEqualTo(facts)
    }

    private fun simpleSearch(): List<ChuckNorrisFact> = runBlocking {
        dataSource.search("Norris")
    }
}
