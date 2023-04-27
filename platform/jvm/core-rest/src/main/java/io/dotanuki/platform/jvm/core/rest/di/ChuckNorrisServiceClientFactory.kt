package io.dotanuki.platform.jvm.core.rest.di

import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceBuilder
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient
import io.dotanuki.platform.jvm.core.rest.HttpResilience

object ChuckNorrisServiceClientFactory {

    private var memoized: ChuckNorrisServiceClient? = null

    context (ApiUrlFactory)
    fun create(): ChuckNorrisServiceClient =
        memoized ?: newClient(apiUrl).apply { memoized = this }

    private fun newClient(apiUrl: String): ChuckNorrisServiceClient {
        val resilience = HttpResilience.createDefault()
        val service = ChuckNorrisServiceBuilder.build(apiUrl, resilience)
        return ChuckNorrisServiceClient(service, resilience)
    }
}
