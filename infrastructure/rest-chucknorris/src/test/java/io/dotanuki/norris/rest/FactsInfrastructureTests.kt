package io.dotanuki.norris.rest

import io.dotanuki.norris.rest.errors.RemoteServiceIntegrationError
import io.dotanuki.norris.rest.model.ChuckNorrisFact
import io.dotanuki.norris.rest.model.RelatedCategory.Available
import io.dotanuki.norris.rest.model.RelatedCategory.Missing
import io.dotanuki.norris.rest.util.InfrastructureRule
import io.dotanuki.norris.rest.util.assertErrorTransformed
import io.dotanuki.norris.rest.util.loadFile
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class FactsInfrastructureTests {

    @get:Rule val rule = InfrastructureRule()

    private lateinit var infrastructure: FactsInfrastructure

    @Before fun `before each test`() {
        infrastructure = FactsInfrastructure(rule.api)
    }

    @Test fun `should handle no results properly`() {

        rule.defineScenario(
            status = 200,
            response = loadFile("200_search_no_results.json")
        )

        val noFacts = emptyList<ChuckNorrisFact>()
        assertThat(simpleSearch()).isEqualTo(noFacts)
    }

    @Test fun `should handle client issue`() {

        rule.defineScenario(
            status = 404,
            response = loadFile("404_wrong_path.json")
        )

        assertErrorTransformed(
            whenRunning = this::simpleSearch,
            expected = RemoteServiceIntegrationError.ClientOrigin
        )
    }

    @Test fun `should handle remote system issue`() {

        rule.defineScenario(
            status = 500
        )

        assertErrorTransformed(
            whenRunning = this::simpleSearch,
            expected = RemoteServiceIntegrationError.RemoteSystem
        )
    }

    @Test fun `should handle broken contract`() {

        rule.defineScenario(
            status = 200,
            response = loadFile("200_search_broken_contract.json")

        )

        assertErrorTransformed(
            whenRunning = this::simpleSearch,
            expected = RemoteServiceIntegrationError.UnexpectedResponse
        )
    }

    @Test fun `should fetch facts with valid query term`() {

        rule.defineScenario(
            status = 200,
            response = loadFile("200_search_with_results.json")
        )

        val facts = listOf(
            ChuckNorrisFact(
                id = "lhan43nqsgowtaffzxouua",
                shareableUrl = "https://api.chucknorris.io/jokes/lhan43nqsgowtaffzxouua",
                textual = "Police label anyone attacking Chuck Norris as a Code 45-11.... A suicide.",
                category = Missing
            ),
            ChuckNorrisFact(
                id = "2wzginmks8azrbaxnamxdw",
                shareableUrl = "https://api.chucknorris.io/jokes/2wzginmks8azrbaxnamxdw",
                textual = "Every SQL statement that Chuck Norris codes has an implicit \"COMMIT\" in its end.",
                category = Available("dev")
            )
        )

        assertThat(simpleSearch()).isEqualTo(facts)
    }

    @Test fun `should fetch categories`() {

        rule.defineScenario(
            status = 200,
            response = loadFile("200_categories.json")
        )

        val expected = listOf(
            Available("career"),
            Available("celebrity"),
            Available("dev")
        )

        val categories = runBlocking {
            infrastructure.availableCategories()
        }

        assertThat(categories).isEqualTo(expected)
    }

    private fun simpleSearch() = runBlocking {
        infrastructure.fetchFacts("Norris")
    }
}