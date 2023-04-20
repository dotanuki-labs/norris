package io.dotanuki.platform.jvm.core.rest.internal

import io.dotanuki.platform.jvm.core.networking.errors.HttpDrivenError
import io.dotanuki.platform.jvm.core.networking.errors.NetworkConnectivityError
import io.dotanuki.platform.jvm.core.rest.HttpResilience
import io.github.resilience4j.core.IntervalFunction
import io.github.resilience4j.kotlin.retry.executeSuspendFunction
import io.github.resilience4j.retry.RetryConfig
import io.github.resilience4j.retry.RetryRegistry

class ResilientExecution(config: HttpResilience) {

    private val retryRegistry = RetryRegistry.ofDefaults()

    private val retryConfig =
        RetryConfig.custom<Any>()
            .maxAttempts(config.retriesAttemptPerRequest)
            .intervalFunction(IntervalFunction.ofExponentialBackoff(config.delayBetweenRetries))
            .retryOnException { it.isManagedError() }
            .build()

    private val retry = retryRegistry.retry("retry-policy", retryConfig)

    suspend fun <T> execute(block: suspend () -> T): T =
        retry.executeSuspendFunction {
            block.invoke()
        }

    private fun Throwable.isManagedError(): Boolean =
        ManagedErrorTransformer.transform(this).let {
            it is HttpDrivenError || it is NetworkConnectivityError
        }
}
