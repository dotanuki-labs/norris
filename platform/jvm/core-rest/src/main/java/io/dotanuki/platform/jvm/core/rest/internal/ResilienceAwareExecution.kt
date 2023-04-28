package io.dotanuki.platform.jvm.core.rest.internal

import io.dotanuki.platform.jvm.core.rest.HttpNetworkingError
import io.dotanuki.platform.jvm.core.rest.HttpResilience
import io.github.resilience4j.core.IntervalFunction
import io.github.resilience4j.kotlin.retry.RetryConfig
import io.github.resilience4j.kotlin.retry.executeSuspendFunction
import io.github.resilience4j.retry.RetryRegistry

internal class ResilienceAwareExecution private constructor(spec: HttpResilience) {

    private val retryRegistry by lazy {
        RetryRegistry.ofDefaults()
    }

    private val exponentialBackoff by lazy {
        IntervalFunction.of(spec.delayBetweenRetries)
    }

    private val retryConfig by lazy {
        RetryConfig {
            maxAttempts(spec.retriesAttemptPerRequest)
            retryOnException { it is HttpNetworkingError.Connectivity }
            intervalFunction(exponentialBackoff)
        }
    }

    private val retryRunner by lazy {
        retryRegistry.retry("retry-policy", retryConfig)
    }

    suspend operator fun <T> invoke(block: suspend () -> T): T =
        retryRunner.executeSuspendFunction {
            NetworkingErrorAwareExecution {
                block.invoke()
            }
        }

    companion object {
        fun create(spec: HttpResilience) = ResilienceAwareExecution(spec)
    }
}
