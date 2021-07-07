package io.dotanuki.norri.facts

import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.facts.data.FactsDataSource
import io.dotanuki.norris.facts.domain.ChuckNorrisFact
import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError
import io.dotanuki.testing.files.loadFile
import io.dotanuki.testing.rest.RestInfrastructureRule
import io.dotanuki.testing.rest.wireRestApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class FactsDataSourceTests {

    @get:Rule val restInfrastructure = RestInfrastructureRule()

    private lateinit var dataSource: FactsDataSource

    @Before fun `before each test`() {
        val api = restInfrastructure.server.wireRestApi()
        dataSource = FactsDataSource(api)
    }

    @Test fun `should handle no results properly`() {

        restInfrastructure.restScenario(
            status = 200,
            response = loadFile("200_search_no_results.json")
        )

        val noFacts = emptyList<ChuckNorrisFact>()
        assertThat(simpleSearch()).isEqualTo(noFacts)
    }

    @Test fun `should handle downstream error`() {

        restInfrastructure.restScenario(status = 500)

        assertErrorTransformed(
            whenRunning = this::simpleSearch,
            expected = RemoteServiceIntegrationError.RemoteSystem
        )
    }

    @Test fun `should fetch facts with valid query term`() {

        restInfrastructure.restScenario(
            status = 200,
            response = loadFile("200_search_with_results.json")
        )

        val facts = listOf(
            ChuckNorrisFact(
                id = "lhan43nqsgowtaffzxouua",
                shareableUrl = "https://api.chucknorris.io/jokes/lhan43nqsgowtaffzxouua",
                textual = "Police label anyone attacking Chuck Norris as a Code 45-11.... A suicide.",
            ),
            ChuckNorrisFact(
                id = "2wzginmks8azrbaxnamxdw",
                shareableUrl = "https://api.chucknorris.io/jokes/2wzginmks8azrbaxnamxdw",
                textual = "SQL statements that Chuck Norris code have implicit COMMITs in its end.",
            )
        )

        assertThat(simpleSearch()).isEqualTo(facts)
    }

    private fun simpleSearch(): List<ChuckNorrisFact> = runBlocking {
        dataSource.search("Norris")
    }

    private fun unwrapCaughtError(result: Result<*>): Throwable =
        result.exceptionOrNull() ?: throw IllegalArgumentException("Not an error")

    private fun assertErrorTransformed(expected: Throwable, whenRunning: () -> Any) {
        val result = runCatching { whenRunning.invoke() }
        val unwrapped = unwrapCaughtError(result)
        assertThat(unwrapped).isEqualTo(expected)
    }
}
