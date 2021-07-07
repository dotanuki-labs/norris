package io.dotanuki.testing.rest

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.ExternalResource

class RestInfrastructureRule(private val customPort: Int? = null) : ExternalResource() {

    lateinit var server: MockWebServer

    override fun before() {
        super.before()
        server = MockWebServer()

        customPort?.let { server.start(it) }
    }

    override fun after() {
        server.shutdown()
        super.after()
    }

    fun restScenario(status: Int, response: String = "") {

        server.enqueue(
            MockResponse().apply {
                setResponseCode(status)
                setBody(response)
            }
        )
    }
}
