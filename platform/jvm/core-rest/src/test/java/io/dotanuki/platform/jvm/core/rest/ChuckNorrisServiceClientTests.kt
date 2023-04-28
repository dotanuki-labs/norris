package io.dotanuki.platform.jvm.core.rest

import com.google.common.truth.Truth.assertThat
import eu.rekawek.toxiproxy.Proxy
import eu.rekawek.toxiproxy.ToxiproxyClient
import io.dotanuki.platform.jvm.core.rest.di.ChuckNorrisServiceClientFactory
import io.dotanuki.platform.jvm.core.rest.util.ToxicityLevel
import io.dotanuki.platform.jvm.core.rest.util.bandwidth
import io.dotanuki.platform.jvm.core.rest.util.limitData
import io.dotanuki.platform.jvm.core.rest.util.setToxicity
import io.dotanuki.platform.jvm.core.rest.util.timeout
import io.dotanuki.platform.jvm.testing.rest.RestDataBuilder
import java.time.Duration
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse
import org.mockserver.model.HttpResponse.response
import org.testcontainers.containers.MockServerContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.ToxiproxyContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

class ChuckNorrisServiceClientTests {

    private val testResilienceSpec by lazy {
        HttpResilience(
            retriesAttemptPerRequest = 3,
            delayBetweenRetries = Duration.ofSeconds(1L),
            timeoutForHttpRequest = Duration.ofSeconds(5L)
        )
    }

    private val mockServerPort = 1080
    private val toxyProxyPort = 8666

    private val mockServerImage by lazy {
        val mockServerVersion = MockServerClient::class.java.getPackage().implementationVersion
        val componentName = "mockserver/mockserver"
        val imageTag = "mockserver-$mockServerVersion"
        DockerImageName.parse(componentName).withTag(imageTag)
    }

    private val toxyProxyImage by lazy {
        val componentName = "ghcr.io/shopify/toxiproxy"
        val imageTag = "2.5.0"
        DockerImageName.parse(componentName).withTag(imageTag)
    }

    @get:Rule val network: Network = Network.newNetwork()

    @get:Rule val mockServerContainer: MockServerContainer =
        MockServerContainer(mockServerImage)
            .withNetwork(network)
            .withNetworkAliases("mockserver")
            .waitingFor(
                // https://github.com/testcontainers/testcontainers-java/issues/6647
                Wait.forHttp("/mockserver/status")
                    .withMethod("PUT")
                    .forStatusCode(200)
                    .withStartupTimeout(Duration.ofMinutes(2L))
            )

    @get:Rule val toxiProxyContainer: ToxiproxyContainer =
        ToxiproxyContainer(toxyProxyImage)
            .withNetwork(network)
            .dependsOn(mockServerContainer)

    private lateinit var toxiproxy: Proxy
    private lateinit var chuckNorrisClient: ChuckNorrisServiceClient
    private lateinit var mockServerClient: MockServerClient

    @Before fun `before each test`() {
        val toxiproxyClient = toxiProxyContainer.let { ToxiproxyClient(it.host, it.controlPort) }
        toxiproxy = toxiproxyClient.createProxy(
            "mockserver",
            "0.0.0.0:$toxyProxyPort",
            "mockserver:$mockServerPort"
        )

        val url = toxiProxyContainer.let { "http://${it.host}:${it.getMappedPort(toxyProxyPort)}" }
        chuckNorrisClient = ChuckNorrisServiceClientFactory.create(url, testResilienceSpec)
        mockServerClient = with(mockServerContainer) { MockServerClient(host, serverPort) }
    }

    @Test fun `should capture connection spikes as logical errors`() {
        mockServerClient.given(categoriesRequest()).respond(successfulResponse())
        toxiproxy.limitData(numberOfBytes = 10).setToxicity(ToxicityLevel.EXTREME)

        runBlocking {
            runCatching { chuckNorrisClient.categories() }
                .onSuccess { throw AssertionError("Expecting a failure due the toxicity level used on this test") }
                .onFailure {
                    assertThat(it).isEqualTo(HttpNetworkingError.Connectivity.ConnectionSpike)
                }
        }
    }

    @Test fun `should not recover from HTTP errors`() {
        mockServerClient.given(categoriesRequest()).respond(errorResponse())

        runBlocking {
            runCatching { chuckNorrisClient.categories() }
                .onSuccess { throw AssertionError("Expecting an error from this execution") }
                .onFailure {
                    val expected = HttpNetworkingError.Restful.Server(500)
                    assertThat(it).isEqualTo(expected)
                }
        }
    }

    @Test fun `should recover from latency in the network`() {
        mockServerClient.given(categoriesRequest()).respond(successfulResponse())
        toxiproxy.bandwidth(latency = 2000, jitter = 3000).setToxicity(ToxicityLevel.LOW)

        runBlocking {
            val categories = chuckNorrisClient.categories()
            assertThat(categories).isNotEmpty()
        }
    }

    @Test fun `should recover from connection spikes`() {
        mockServerClient.given(categoriesRequest()).respond(successfulResponse())
        toxiproxy.limitData(numberOfBytes = 15).setToxicity(ToxicityLevel.MODERATE)

        runBlocking {
            val categories = chuckNorrisClient.categories()
            assertThat(categories).isNotEmpty()
        }
    }

    @Test fun `should recover from network timeouts`() {
        mockServerClient.given(categoriesRequest()).respond(successfulResponse())

        val forcedTimeout = testResilienceSpec.timeoutForHttpRequest.seconds * 1000 + 2000
        toxiproxy.timeout(forcedTimeout).setToxicity(ToxicityLevel.LOW)

        runBlocking {
            val categories = chuckNorrisClient.categories()
            assertThat(categories).isNotEmpty()
        }
    }

    private fun categoriesRequest(): HttpRequest =
        request().withPath("/jokes/categories")

    private fun successfulResponse(): HttpResponse =
        listOf("code", "dev")
            .let { RestDataBuilder.categoriesJson(it) }
            .let { response().withBody(it).withStatusCode(200) }

    private fun errorResponse(): HttpResponse =
        response().withStatusCode(500)

    private fun MockServerClient.given(request: HttpRequest) =
        `when`(request)
}
