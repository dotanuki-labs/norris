package io.dotanuki.platform.jvm.core.rest.internal

import io.dotanuki.platform.jvm.core.networking.errors.HttpDrivenError
import io.dotanuki.platform.jvm.core.networking.errors.NetworkConnectivityError
import io.github.resilience4j.core.IntervalFunction
import io.github.resilience4j.kotlin.retry.executeSuspendFunction
import io.github.resilience4j.retry.RetryConfig
import io.github.resilience4j.retry.RetryRegistry

internal object ResilientExecution {

    private val retryRegistry = RetryRegistry.ofDefaults()

    private val retryConfig = with(ResilienceConfiguration) {
        RetryConfig.custom<Any>()
            .maxAttempts(RETRY_ATTEMPTS_PER_REQUEST)
            .intervalFunction(IntervalFunction.ofExponentialBackoff(DELAY_BETWEEN_RETRIES))
            .retryOnException { it.isManagedError() }
            .build()
    }

    private val retry = retryRegistry.retry("retry-policy", retryConfig)

    suspend operator fun <T> invoke(block: suspend () -> T): T =
        retry.executeSuspendFunction {
            block.invoke()
        }

    private fun Throwable.isManagedError(): Boolean =
        ManagedErrorTransformer.transform(this).let {
            it is HttpDrivenError || it is NetworkConnectivityError
        }
}
