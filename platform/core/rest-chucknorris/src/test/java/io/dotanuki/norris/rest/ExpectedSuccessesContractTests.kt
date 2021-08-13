package io.dotanuki.norris.rest

import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit.PactProviderRule
import au.com.dius.pact.consumer.junit.PactVerification
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import com.google.common.truth.Truth.assertThat
import io.dotanuki.testing.rest.ChuckNorrisApi
import io.dotanuki.testing.rest.RestDataBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

class ExpectedSuccessesContractTests {

    private val randomPort by lazy {
        Random.nextInt(until = 5000) + 1000
    }

    @get:Rule val pactServer = PactProviderRule(CONTRACT_PROVIDER, "localhost", randomPort, this)

    lateinit var api: ChuckNorrisDotIO

    private val gitFact = "Chuck Norris commits before add on Git"
    private val query = "code"
    private val categories = listOf("code", "dev", "humor")

    @Before fun `before each test`() {
        api = ChuckNorrisApi(pactServer.url)
    }

    @Pact(provider = CONTRACT_PROVIDER, consumer = CONTRACT_CONSUMER)
    fun createPact(builder: PactDslWithProvider?): RequestResponsePact {

        val facts = RestDataBuilder.factsPayload(query, gitFact)
        val categories = RestDataBuilder.suggestionsPayload(categories)

        return requireNotNull(builder)
            .uponReceiving("successful search for facts")
            .path("/jokes/search").method("GET").matchQuery("query", "code")
            .willRespondWith().status(200).body(facts)

            .uponReceiving("successful search for categories")
            .path("/jokes/categories").method("GET")
            .willRespondWith().status(200).body(categories)
            .toPact()
    }

    @Test @PactVerification(CONTRACT_PROVIDER) fun `check contracts`() {

        runBlocking {
            val rawSearch = RawSearch(
                listOf(
                    RawFact(
                        id = RestDataBuilder.FACT_ID,
                        url = RestDataBuilder.FACT_URL,
                        value = gitFact,
                        categories = listOf(query)
                    )
                )
            )

            val rawCategories = RawCategories(categories)

            api.run {
                assertThat(search(query)).isEqualTo(rawSearch)
                assertThat(categories()).isEqualTo(rawCategories)
            }
        }
    }

    companion object {
        const val CONTRACT_PROVIDER = "chucknorris-io"
        const val CONTRACT_CONSUMER = "android-supported-responses"
    }
}
