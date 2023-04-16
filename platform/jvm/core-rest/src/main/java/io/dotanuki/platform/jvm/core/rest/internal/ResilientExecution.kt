package io.dotanuki.platform.jvm.core.rest.internal

import io.dotanuki.platform.jvm.core.networking.errors.NetworkingError
import io.dotanuki.platform.jvm.core.networking.errors.RemoteServiceIntegrationError
import io.github.resilience4j.core.IntervalFunction
import io.github.resilience4j.kotlin.retry.executeSuspendFunction
import io.github.resilience4j.retry.RetryConfig
import io.github.resilience4j.retry.RetryRegistry
import java.time.Duration

internal object ResilientExecution {

    private const val MAX_RETRIES = 3
    private val retryRegistry = RetryRegistry.ofDefaults()
    private val retryInterval = Duration.ofSeconds(2L)

    private val retryConfig =
        RetryConfig.custom<Any>()
            .maxAttempts(MAX_RETRIES)
            .intervalFunction(IntervalFunction.ofExponentialBackoff(retryInterval))
            .retryOnException { it.isManagedError() }
            .build()

    private val retry = retryRegistry.retry("retry-policy", retryConfig)

    suspend operator fun <T> invoke(block: suspend () -> T): T =
        retry.executeSuspendFunction {
            block.invoke()
        }

    private fun Throwable.isManagedError(): Boolean =
        ManagedErrorTransformer.transform(this).let {
            it is RemoteServiceIntegrationError || it is NetworkingError
        }
}
