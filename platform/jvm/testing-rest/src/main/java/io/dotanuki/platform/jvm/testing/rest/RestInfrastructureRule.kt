package io.dotanuki.platform.jvm.testing.rest

import okhttp3.internal.closeQuietly
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.ExternalResource
import java.util.concurrent.TimeUnit

class RestInfrastructureRule(private val customPort: Int? = null) : ExternalResource() {

    lateinit var server: MockWebServer

    override fun before() {
        super.before()
        server = MockWebServer()
        customPort?.let { server.start(it) }
    }

    override fun after() {
        server.closeQuietly()
        server.shutdown()
        super.after()
    }

    fun restScenario(status: Int, response: String = "", delayInSeconds: Long = 0) {

        server.enqueue(
            MockResponse().apply {
                setResponseCode(status)
                setBody(response)
                setBodyDelay(delayInSeconds, TimeUnit.SECONDS)
            }
        )
    }
}
