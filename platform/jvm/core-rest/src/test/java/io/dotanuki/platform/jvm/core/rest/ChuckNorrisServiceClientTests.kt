package io.dotanuki.platform.jvm.core.rest

import com.google.common.truth.Truth.assertThat
import io.dotanuki.platform.jvm.testing.rest.RestDataBuilder
import io.dotanuki.platform.jvm.testing.rest.RestInfrastructureRule
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChuckNorrisServiceClientTests {

    @get:Rule val restInfrastructure = RestInfrastructureRule()

    private lateinit var client: ChuckNorrisServiceClient

    @Before fun `before each test`() {
        val server = restInfrastructure.server.url("/")

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .build()

        val api = RetrofitBuilder(server, okHttpClient).create(ChuckNorrisService::class.java)
        client = ChuckNorrisServiceClient(api)
    }

    @Test fun `should retry when needed`() = runBlocking {

        restInfrastructure.restScenario(status = 503)

        restInfrastructure.restScenario(
            status = 200,
            response = RestDataBuilder.factsPayload("math", "Chuck Norris can divide by zero")
        )

        val search = client.search("math")
        assertThat(search.result).isNotEmpty()
    }
}
