package io.dotanuki.norris.rest.util

import io.dotanuki.norris.networking.RetrofitBuilder
import io.dotanuki.norris.domain.rest.ChuckNorrisDotIO
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.ExternalResource

internal class InfrastructureRule : ExternalResource() {

    lateinit var server: MockWebServer
    lateinit var api: ChuckNorrisDotIO

    override fun before() {
        super.before()
        server = MockWebServer()
        val url = server.url("/").toString()

        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .build()

        api = RetrofitBuilder(url, client).create(ChuckNorrisDotIO::class.java)
    }

    override fun after() {
        server.shutdown()
        super.after()
    }

    fun defineScenario(status: Int, response: String = "") {

        server.enqueue(
            MockResponse().apply {
                setResponseCode(status)
                setBody(response)
            }
        )
    }
}