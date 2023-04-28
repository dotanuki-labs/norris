package io.dotanuki.platform.jvm.core.rest

import io.dotanuki.platform.jvm.core.rest.internal.ResilienceAwareExecution

class ChuckNorrisServiceClient(
    private val service: ChuckNorrisService,
    private val resilience: HttpResilience
) {

    private val resilienceAwareExecution by lazy {
        ResilienceAwareExecution.create(resilience)
    }

    suspend fun categories(): List<String> =
        resilienceAwareExecution {
            service.categories()
        }

    suspend fun search(query: String): RawSearch =
        resilienceAwareExecution {
            service.search(query)
        }
}
