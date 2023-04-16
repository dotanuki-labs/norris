package io.dotanuki.platform.jvm.core.rest

import io.dotanuki.platform.jvm.core.rest.internal.ErrorManagedExecution
import io.dotanuki.platform.jvm.core.rest.internal.ResilientExecution

class ChuckNorrisServiceClient(private val service: ChuckNorrisService) {

    suspend fun categories(): List<String> =
        ErrorManagedExecution {
            ResilientExecution {
                service.categories()
            }
        }

    suspend fun search(query: String): RawSearch =
        ErrorManagedExecution {
            ResilientExecution {
                service.search(query)
            }
        }
}
