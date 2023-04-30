package io.dotanuki.platform.jvm.core.rest

import com.google.common.truth.Truth.assertThat
import eu.rekawek.toxiproxy.Proxy
import eu.rekawek.toxiproxy.ToxiproxyClient
import io.dotanuki.platform.jvm.core.rest.di.ChuckNorrisServiceClientFactory
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

    private val testResilienceSpec by lazy {
        HttpResilience(
            retriesAttemptPerRequest = 3,
            delayBetweenRetries = Duration.ofSeconds(1L),
            timeoutForHttpRequest = Duration.ofSeconds(5L)
        )
    }

    private val toxyProxyPort = 8666

    private val toxyProxyImage by lazy {
        val componentName = "ghcr.io/shopify/toxiproxy"
        val imageTag = "2.5.0"
        DockerImageName.parse(componentName).withTag(imageTag)
    }

    @get:Rule val network: Network = Network.newNetwork()

    @get:Rule val wireMockContainer: WireMockContainer by lazy {
        WireMockContainer()
            .withMapping("categories", loadFile("categories-stub.json"))
            .withNetworkAliases("wiremock")
            .withNetwork(network)
    }

    @get:Rule val toxiProxyContainer: ToxiproxyContainer =
        ToxiproxyContainer(toxyProxyImage).withNetwork(network).dependsOn(wireMockContainer)

    private lateinit var toxiproxy: Proxy
    private lateinit var chuckNorrisClient: ChuckNorrisServiceClient

    @Before fun `before each test`() {
        val toxiproxyClient = toxiProxyContainer.let { ToxiproxyClient(it.host, it.controlPort) }
        toxiproxy = toxiproxyClient.createProxy(
            "mockserver",
            "0.0.0.0:$toxyProxyPort",
            "wiremock:${WireMockContainer.DEFAULT_PORT}"
        )

        val url = toxiProxyContainer.let { "http://${it.host}:${it.getMappedPort(toxyProxyPort)}" }
        chuckNorrisClient = ChuckNorrisServiceClientFactory.create(url, testResilienceSpec)
    }

    @Test fun `should capture connection spikes as logical errors`() {
        toxiproxy.disable()

        runBlocking {
            runCatching { chuckNorrisClient.categories() }
                .onSuccess { throw AssertionError("Expecting a failure due the toxicity level used on this test") }
                .onFailure {
                    assertThat(it).isEqualTo(HttpNetworkingError.Connectivity.ConnectionSpike)
                }
        }
    }

    @Test fun `should not recover from HTTP errors`() {
        runBlocking {
            runCatching { chuckNorrisClient.search("invalid-search") }
                .onSuccess { throw AssertionError("Expecting an error from this execution") }
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
        toxiproxy.limitData(numberOfBytes = 15).setToxicity(ToxicityLevel.MODERATE)

        runBlocking {
            val categories = chuckNorrisClient.categories()
            assertThat(categories).isNotEmpty()
        }
    }

    @Test fun `should recover from network timeouts`() {
        val forcedTimeout = testResilienceSpec.timeoutForHttpRequest.seconds * 1000 + 2000
        toxiproxy.timeout(forcedTimeout).setToxicity(ToxicityLevel.LOW)

        runBlocking {
            val categories = chuckNorrisClient.categories()
            assertThat(categories).isNotEmpty()
        }
    }
}
