package io.dotanuki.platform.jvm.core.rest.internal

import io.dotanuki.platform.jvm.core.networking.errors.HttpDrivenError
import io.dotanuki.platform.jvm.core.networking.errors.NetworkConnectivityError
import io.dotanuki.platform.jvm.core.rest.HttpResilience
import io.github.resilience4j.core.IntervalFunction
import io.github.resilience4j.kotlin.retry.RetryConfig
import io.github.resilience4j.kotlin.retry.executeSuspendFunction
import io.github.resilience4j.retry.RetryRegistry

class ResilienceAware private constructor(spec: HttpResilience) {

    private val retryRegistry by lazy {
        RetryRegistry.ofDefaults()
    }

    private val exponentialBackoff by lazy {
        IntervalFunction.ofExponentialBackoff(spec.delayBetweenRetries)
    }

    private val retryConfig by lazy {
        RetryConfig {
            maxAttempts(spec.retriesAttemptPerRequest)
            retryOnException { it.isManagedError() }
            intervalFunction(exponentialBackoff)
        }
    }

    private val retryRunner by lazy {
        retryRegistry.retry("retry-policy", retryConfig)
    }

    suspend operator fun <T> invoke(block: suspend () -> T): T =
        retryRunner.executeSuspendFunction {
            block.invoke()
        }

    private fun Throwable.isManagedError(): Boolean =
        ManagedErrorTransformer.transform(this).let {
            it is HttpDrivenError || it is NetworkConnectivityError
        }

    companion object {
        fun create(spec: HttpResilience) = ResilienceAware(spec)
    }
}
