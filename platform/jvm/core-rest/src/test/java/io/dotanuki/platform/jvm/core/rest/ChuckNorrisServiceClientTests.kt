package io.dotanuki.platform.jvm.core.rest

import com.google.common.truth.Truth.assertThat
import eu.rekawek.toxiproxy.Proxy
import eu.rekawek.toxiproxy.ToxiproxyClient
import io.dotanuki.platform.jvm.core.rest.util.ToxicityLevel
import io.dotanuki.platform.jvm.core.rest.util.WireMockContainer
import io.dotanuki.platform.jvm.core.rest.util.bandwidth
import io.dotanuki.platform.jvm.core.rest.util.limitData
import io.dotanuki.platform.jvm.core.rest.util.setToxicity
import io.dotanuki.platform.jvm.core.rest.util.timeout
import io.dotanuki.platform.jvm.testing.helpers.files.loadFile
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.testcontainers.containers.Network
import org.testcontainers.containers.ToxiproxyContainer
import org.testcontainers.utility.DockerImageName
import java.time.Duration

class ChuckNorrisServiceClientTests {

    private val resilienceSpec by lazy {
        HttpResilience.createDefault().copy(
            timeoutForHttpRequest = Duration.ofSeconds(5L)
        )
    }

    private val toxyProxyImage by lazy {
        val componentName = "ghcr.io/shopify/toxiproxy"
        val imageTag = "2.5.0"
        DockerImageName.parse(componentName).withTag(imageTag)
    }

    private val wireMockServerImage by lazy {
        val componentName = "wiremock/wiremock"
        val imageTag = "2.32.0"
        DockerImageName.parse(componentName).withTag(imageTag)
    }

    private val wireMockNetworkAlias = "wiremock"

    @get:Rule val network: Network = Network.newNetwork()

    @get:Rule val wireMockContainer: WireMockContainer =
        WireMockContainer(wireMockServerImage)
            .withStubMapping("categories", loadFile("categories-stub.json"))
            .withNetworkAliases(wireMockNetworkAlias)
            .withNetwork(network)

    @get:Rule val toxiProxyContainer: ToxiproxyContainer =
        ToxiproxyContainer(toxyProxyImage)
            .withNetwork(network)
            .dependsOn(wireMockContainer)

    private lateinit var toxiproxy: Proxy
    private lateinit var chuckNorrisClient: ChuckNorrisServiceClient

    @Before fun `before each test`() {
        val toxyProxyPort = 8666
        val wireMockPort = 8080

        val toxiproxyClient = toxiProxyContainer.let { ToxiproxyClient(it.host, it.controlPort) }
        toxiproxy = toxiproxyClient.createProxy(
            "wiremock",
            "0.0.0.0:$toxyProxyPort",
            "$wireMockNetworkAlias:$wireMockPort"
        )

        val baseUrl = toxiProxyContainer.let { "http://${it.host}:${it.getMappedPort(toxyProxyPort)}" }
        val service = ChuckNorrisServiceBuilder.build(baseUrl, resilienceSpec)
        chuckNorrisClient = ChuckNorrisServiceClient(service, resilienceSpec)
    }

    @Test fun `should capture connection spikes as logical errors`() {
        // Forces an interruption upfront
        toxiproxy.disable()

        runBlocking {
            runCatching { chuckNorrisClient.categories() }
                .onSuccess { unexpectedOutcome() }
                .onFailure {
                    assertThat(it).isEqualTo(HttpNetworkingError.Connectivity.ConnectionSpike)
                }
        }
    }

    @Test fun `should not recover from HTTP errors`() {
        runBlocking {
            runCatching { chuckNorrisClient.search("not-stubbed") }
                .onSuccess { unexpectedOutcome() }
                .onFailure {
                    val expected = HttpNetworkingError.Restful.Client(404)
                    assertThat(it).isEqualTo(expected)
                }
        }
    }

    @Test fun `should recover from latency in the network`() {
        toxiproxy.bandwidth(latency = 2000, jitter = 3000).setToxicity(ToxicityLevel.LOW)

        runBlocking {
            val categories = chuckNorrisClient.categories()
            assertThat(categories).isNotEmpty()
        }
    }

    @Test fun `should recover from connection spikes`() {
        toxiproxy.limitData(numberOfBytes = 15).setToxicity(ToxicityLevel.LOW)

        runBlocking {
            val categories = chuckNorrisClient.categories()
            assertThat(categories).isNotEmpty()
        }
    }

    @Test fun `should recover from network timeouts`() {
        val forcedTimeout = resilienceSpec.timeoutForHttpRequest.seconds * 1000 + 2000
        toxiproxy.timeout(forcedTimeout).setToxicity(ToxicityLevel.LOW)

        runBlocking {
            val categories = chuckNorrisClient.categories()
            assertThat(categories).isNotEmpty()
        }
    }

    private fun unexpectedOutcome(): Nothing {
        throw AssertionError("Expecting an error from this execution")
    }
}
