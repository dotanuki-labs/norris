package io.dotanuki.platform.jvm.core.rest

import io.dotanuki.platform.jvm.core.rest.internal.ErrorManagedExecution
import io.dotanuki.platform.jvm.core.rest.internal.ResilientExecution

class ChuckNorrisServiceClient(
    private val service: ChuckNorrisService,
    private val resilience: HttpResilience
) {

    private val resilientExecution by lazy {
        ResilientExecution(resilience)
    }

    suspend fun categories(): List<String> =
        ErrorManagedExecution {
            resilientExecution.execute {
                service.categories()
            }
        }

    suspend fun search(query: String): RawSearch =
        ErrorManagedExecution {
            resilientExecution.execute {
                service.search(query)
            }
        }
}
