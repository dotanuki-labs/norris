package io.dotanuki.norris.rest

import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit.PactProviderRule
import au.com.dius.pact.consumer.junit.PactVerification
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.annotations.Pact
import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError.ClientOrigin
import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError.RemoteSystem
import io.dotanuki.testing.rest.ChuckNorrisApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

class ExpectedErrorsContractTests {

    private val randomPort by lazy {
        Random.nextInt(until = 5000) + 1000
    }

    @get:Rule val pactServer = PactProviderRule(CONTRACT_PROVIDER, "localhost", randomPort, this)

    lateinit var api: ChuckNorrisDotIO

    private val clientError =
        """
        {
          "timestamp": "2019-06-10T15:55:54.875Z",
          "status": 404,
          "error": "Not Found",
          "message": "Joke with term \"abcded\" not found.",
          "path": "/jokes/search"
        }
        """.trimIndent()

    private val serverError =
        """
        {
            "message": "Internal Server Error"
        }
        """.trimIndent()

    @Before fun `before each test`() {
        api = ChuckNorrisApi(pactServer.url)
    }

    @Pact(provider = CONTRACT_PROVIDER, consumer = CONTRACT_CONSUMER)
    fun createPact(builder: PactDslWithProvider?): RequestResponsePact {

        return requireNotNull(builder)
            .uponReceiving("error when hitting an invalid endpoint")
            .path("/jokes/search").method("GET").matchQuery("query", "code")
            .willRespondWith().status(404).body(clientError)

            .uponReceiving("error when service unavailable")
            .path("/jokes/categories").method("GET")
            .willRespondWith().status(503).body(serverError)
            .toPact()
    }

    @Test @PactVerification(CONTRACT_PROVIDER) fun `check contracts`() {

        runBlocking {
            api.run {

                val notFound = catchError { managedExecution { search("code") } }
                val serverDown = catchError { managedExecution { categories() } }

                assertThat(notFound).isEqualTo(ClientOrigin)
                assertThat(serverDown).isEqualTo(RemoteSystem)
            }
        }
    }

    private fun catchError(whenRunning: suspend () -> Any): Throwable {
        val result = runCatching { runBlocking { whenRunning.invoke() } }
        return result.exceptionOrNull() ?: throw IllegalArgumentException("Not an error")
    }

    companion object {
        const val CONTRACT_PROVIDER = "chucknorris-io"
        const val CONTRACT_CONSUMER = "android-supported-errors"
    }
}
