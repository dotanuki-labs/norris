package io.dotanuki.platform.jvm.core.rest

import io.dotanuki.platform.jvm.core.networking.errors.NetworkingError
import io.dotanuki.platform.jvm.core.networking.errors.RemoteServiceIntegrationError
import io.dotanuki.platform.jvm.core.networking.managedExecution
import io.dotanuki.platform.jvm.core.networking.transformers.AggregatedErrorTransformer
import io.github.resilience4j.core.IntervalFunction
import io.github.resilience4j.kotlin.retry.executeSuspendFunction
import io.github.resilience4j.retry.RetryConfig
import io.github.resilience4j.retry.RetryRegistry
import java.time.Duration


class ChuckNorrisServiceClient(private val service: ChuckNorrisService) {

    val maxRetries = 5
    val retryRegistry = RetryRegistry.ofDefaults()

    var intervalWithExponentialBackoff: IntervalFunction = IntervalFunction
        .ofExponentialBackoff(Duration.ofSeconds(3L))

    var config: RetryConfig = RetryConfig.custom<Any>()
        .maxAttempts(maxRetries)
        .intervalFunction(intervalWithExponentialBackoff)
        .retryOnException {
            val transformed = AggregatedErrorTransformer.transform(it)
            transformed is RemoteServiceIntegrationError || transformed is NetworkingError
        }
        .build()

    val retry = retryRegistry.retry("retry-policy", config)

    suspend fun categories(): List<String> =
        retry.executeSuspendFunction {
            managedExecution { service.categories() }
        }

    suspend fun search(query: String): RawSearch =
        managedExecution {
            retry.executeSuspendFunction {
                service.search(query)
            }
        }
}
