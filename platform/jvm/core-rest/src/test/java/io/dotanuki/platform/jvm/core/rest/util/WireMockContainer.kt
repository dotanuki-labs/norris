package io.dotanuki.platform.jvm.core.rest.util

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.images.builder.Transferable

class WireMockContainer : GenericContainer<WireMockContainer>("$DEFAULT_IMAGE_NAME:$DEFAULT_TAG") {

    private val mappingStubs: MutableMap<String, MockStub> = mutableMapOf()

    private val customWaitStrategy by lazy {
        Wait
            .forHttp("/__admin/mappings")
            .withMethod("GET")
            .forStatusCode(200)
    }

    fun withMapping(name: String, json: String): WireMockContainer {
        mappingStubs[name] = MockStub(name, json)
        return this
    }

    override fun configure() {
        super.configure()
        withExposedPorts(DEFAULT_PORT)
        waitingFor(customWaitStrategy)

        for (stub in mappingStubs.values) {
            withCopyToContainer(Transferable.of(stub.json), MAPPINGS_DIR + stub.name + ".json")
        }
    }

    private data class MockStub(val name: String, val json: String)

    companion object {
        const val DEFAULT_PORT = 8080
        private const val DEFAULT_IMAGE_NAME = "wiremock/wiremock"
        private const val DEFAULT_TAG = "latest"
        private const val MAPPINGS_DIR = "/home/wiremock/mappings/"
    }
}
