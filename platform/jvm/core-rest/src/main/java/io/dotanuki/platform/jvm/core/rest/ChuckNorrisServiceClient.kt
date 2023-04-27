package io.dotanuki.platform.jvm.core.rest

import io.dotanuki.platform.jvm.core.rest.internal.ManagedErrorAware
import io.dotanuki.platform.jvm.core.rest.internal.ResilienceAware

class ChuckNorrisServiceClient(
    private val service: ChuckNorrisService,
    private val resilience: HttpResilience
) {

    private val resilienceAware by lazy {
        ResilienceAware.create(resilience)
    }

    suspend fun categories(): List<String> =
        ManagedErrorAware {
            resilienceAware {
                service.categories()
            }
        }

    suspend fun search(query: String): RawSearch =
        ManagedErrorAware {
            resilienceAware {
                service.search(query)
            }
        }
}
