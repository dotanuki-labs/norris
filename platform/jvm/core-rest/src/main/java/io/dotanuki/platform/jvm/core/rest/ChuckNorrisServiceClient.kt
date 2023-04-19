package io.dotanuki.platform.jvm.core.rest

import io.dotanuki.platform.jvm.core.rest.internal.ErrorManagedExecution
import io.dotanuki.platform.jvm.core.rest.internal.ResilienceConfiguration
import io.dotanuki.platform.jvm.core.rest.internal.ResilientExecution

class ChuckNorrisServiceClient(
    private val service: ChuckNorrisService,
    private val configuration: ResilienceConfiguration = ResilienceConfiguration.createDefault()
) {
    suspend fun categories(): List<String> =
        ErrorManagedExecution {
            ResilientExecution(configuration).execute {
                service.categories()
            }
        }

    suspend fun search(query: String): RawSearch =
        ErrorManagedExecution {
            ResilientExecution(configuration).execute {
                service.search(query)
            }
        }
}
