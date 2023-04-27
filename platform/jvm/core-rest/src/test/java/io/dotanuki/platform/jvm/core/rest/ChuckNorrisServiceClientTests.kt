package io.dotanuki.platform.jvm.core.rest

import com.google.common.truth.Truth.assertThat
import eu.rekawek.toxiproxy.Proxy
import eu.rekawek.toxiproxy.ToxiproxyClient
import io.dotanuki.platform.jvm.core.networking.errors.NetworkConnectivityError
import io.dotanuki.platform.jvm.core.rest.util.ToxicityLevel
import io.dotanuki.platform.jvm.core.rest.util.bandwidth
import io.dotanuki.platform.jvm.core.rest.util.limitData
import io.dotanuki.platform.jvm.core.rest.util.setToxicity
import io.dotanuki.platform.jvm.core.rest.util.timeout
import io.dotanuki.platform.jvm.testing.rest.FakeHttpResilience
import io.dotanuki.platform.jvm.testing.rest.RestDataBuilder
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockserver.client.MockServerClient
import org.mockserver.mock.action.ExpectationResponseCallback
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

    private val categories by lazy {
        RestDataBuilder.suggestionsPayload(
            listOf("math", "code", "humor")
        )
    }

    private val testResilienceSpec by lazy {
        FakeHttpResilience.create()
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
                Wait.forHttp("/mockserver/status").withMethod("PUT").forStatusCode(200)
            )

    @get:Rule val toxiProxyContainer: ToxiproxyContainer =
        ToxiproxyContainer(toxyProxyImage)
            .withNetwork(network)

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
        val service = ChuckNorrisServiceBuilder.build(url, testResilienceSpec)
        chuckNorrisClient = ChuckNorrisServiceClient(service, testResilienceSpec)

        mockServerClient = with(mockServerContainer) { MockServerClient(host, serverPort) }
    }

    @Test fun `should recover from HTTP errors`() {
        val url = mockServerContainer.let { "http://${it.host}:${it.firstMappedPort}" }
        val service = ChuckNorrisServiceBuilder.build(url, testResilienceSpec)
        val client = ChuckNorrisServiceClient(service, testResilienceSpec)

        var attempts = 0

        val aFewFailures = ExpectationResponseCallback {
            if (attempts < testResilienceSpec.retriesAttemptPerRequest) {
                errorResponse()
                attempts += 1
            }
            successfulResponse()
        }

        mockServerClient.on(categoriesRequest()).respond(aFewFailures)

        runBlocking {
            val categories = client.categories()
            assertThat(categories).isNotEmpty()
        }
    }

    @Test fun `should succeed over high connection latency`() {
        mockServerClient.on(categoriesRequest()).respond(successfulResponse())
        toxiproxy.bandwidth(latency = 2000, jitter = 3000).setToxicity(ToxicityLevel.HIGH)

        runBlocking {
            val categories = chuckNorrisClient.categories()
            assertThat(categories).isNotEmpty()
        }
    }

    @Test fun `should resist to connection spikes`() {
        mockServerClient.on(categoriesRequest()).respond(successfulResponse())
        toxiproxy.limitData(numberOfBytes = 15).setToxicity(ToxicityLevel.MEDIUM)

        runBlocking {
            val categories = chuckNorrisClient.categories()
            assertThat(categories).isNotEmpty()
        }
    }

    @Test fun `should translate connection spike as managed error`() {
        mockServerClient.on(categoriesRequest()).respond(successfulResponse())
        toxiproxy.limitData(numberOfBytes = 10).setToxicity(ToxicityLevel.EXTREME)

        runBlocking {
            runCatching { chuckNorrisClient.categories() }
                .onSuccess { throw AssertionError("Expecting a failure due the toxicity level used on this test") }
                .onFailure {
                    assertThat(it).isEqualTo(NetworkConnectivityError.ConnectionSpike)
                }
        }
    }

    @Test fun `should resist to connection timeouts`() {
        mockServerClient.on(categoriesRequest()).respond(successfulResponse())

        val forced = testResilienceSpec.timeoutForHttpRequest.seconds * 1000 + 2000
        toxiproxy.timeout(forced).setToxicity(ToxicityLevel.LOW)

        runBlocking {
            val categories = chuckNorrisClient.categories()
            assertThat(categories).isNotEmpty()
        }
    }

    private fun categoriesRequest(): HttpRequest =
        request().withPath("/jokes/categories")

    private fun successfulResponse(): HttpResponse =
        response().withBody(categories).withStatusCode(200)

    private fun errorResponse(): HttpResponse =
        response().withStatusCode(500)

    private fun MockServerClient.on(request: HttpRequest) =
        `when`(request)
}
