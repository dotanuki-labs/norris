package io.dotanuki.platform.jvm.core.rest.di

import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceBuilder
import io.dotanuki.platform.jvm.core.rest.HttpResilience
import io.dotanuki.platform.jvm.core.rest.RestClient

object RestClientFactory {
    // Note : adding a context to an object and define a property based on it
    // is not fully supported by the Kotlin compiler at the current state
    private var memoized: RestClient? = null

    context (ApiUrlFactory)
    fun create(): RestClient =
        memoized ?: newClient(apiUrl).apply { memoized = this }

    private fun newClient(apiUrl: String): RestClient {
        val resilience = HttpResilience.createDefault()
        val service = ChuckNorrisServiceBuilder.build(apiUrl, resilience)
        return RestClient(service, resilience)
    }
}
