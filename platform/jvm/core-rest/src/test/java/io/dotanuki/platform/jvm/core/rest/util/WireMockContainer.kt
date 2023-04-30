package io.dotanuki.platform.jvm.core.rest.util

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.images.builder.Transferable
import org.testcontainers.utility.DockerImageName

class WireMockContainer(imageName: DockerImageName) : GenericContainer<WireMockContainer>(imageName) {

    private val stubs = mutableListOf<Pair<String, String>>()

    private val customWaitStrategy by lazy {
        Wait.forHttp("/__admin/mappings").withMethod("GET").forStatusCode(200)
    }

    fun withStubMapping(stubName: String, jsonContent: String): WireMockContainer {
        stubs += Pair(stubName, jsonContent)
        return this
    }

    override fun configure() {
        super.configure()
        withExposedPorts(DEFAULT_PORT)
        waitingFor(customWaitStrategy)

        stubs.forEach { (name, jsonContent) ->
            withCopyToContainer(
                Transferable.of(jsonContent), "$WIREMOCK_STUBS_FOLDER$name.json"
            )
        }
    }

    companion object {
        private const val DEFAULT_PORT = 8080
        private const val WIREMOCK_STUBS_FOLDER = "/home/wiremock/mappings/"
    }
}
