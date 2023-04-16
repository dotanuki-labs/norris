package io.dotanuki.platform.jvm.core.rest

import com.google.common.truth.Truth.assertThat
import io.dotanuki.platform.jvm.testing.helpers.files.loadFile
import io.dotanuki.platform.jvm.testing.rest.RestInfrastructureRule
import io.dotanuki.platform.jvm.testing.rest.wireRestApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChuckNorrisServiceClientTests {

    @get:Rule val restInfrastructure = RestInfrastructureRule()

    private lateinit var client: ChuckNorrisServiceClient

    @Before fun `before each test`() {
        val api = restInfrastructure.server.wireRestApi()
        client = ChuckNorrisServiceClient(api)
    }

    @Test fun `should retry when needed`() {

        restInfrastructure.restScenario(status = 503)
        restInfrastructure.restScenario(status = 503)
        restInfrastructure.restScenario(status = 503)
        restInfrastructure.restScenario(status = 503)

        restInfrastructure.restScenario(
            status = 200,
            response = loadFile("200_search_with_results.json")
        )

        runBlocking {
            val search = client.search("math")
            assertThat(search.result).isNotEmpty()
        }
    }
}
