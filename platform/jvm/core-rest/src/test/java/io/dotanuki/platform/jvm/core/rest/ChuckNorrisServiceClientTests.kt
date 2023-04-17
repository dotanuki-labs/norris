package io.dotanuki.platform.jvm.core.rest

import com.google.common.truth.Truth.assertThat
import io.dotanuki.platform.jvm.core.rest.internal.ResilienceConfiguration.HTTP_REQUEST_TIMEOUT
import io.dotanuki.platform.jvm.testing.rest.RestDataBuilder
import io.dotanuki.platform.jvm.testing.rest.RestInfrastructureRule
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChuckNorrisServiceClientTests {

    @get:Rule val restInfrastructure = RestInfrastructureRule()

    private lateinit var client: ChuckNorrisServiceClient

    private val category = "math"
    private val fact = "Chuck Norris can divide by zero"
    private val fakePayload = RestDataBuilder.factsPayload(category, fact)

    @Before fun `before each test`() {
        val url = restInfrastructure.server.url("/")
        val api = RetrofitBuilder(url).create(ChuckNorrisService::class.java)
        client = ChuckNorrisServiceClient(api)
    }

    @Test fun `should retry when failing on previous request`() = runBlocking {

        restInfrastructure.restScenario(status = 503)
        restInfrastructure.restScenario(200, fakePayload)

        val search = client.search(category)
        assertThat(search.result).isNotEmpty()
    }

    @Test fun `should retry when timing out on previous request`() = runBlocking {

        val additionalDelayInSeconds = 5
        val totalDelay = HTTP_REQUEST_TIMEOUT.seconds + additionalDelayInSeconds
        restInfrastructure.restScenario(200, fakePayload, totalDelay)
        restInfrastructure.restScenario(200, fakePayload)

        val search = client.search(category)
        assertThat(search.result).isNotEmpty()
    }
}
