package io.dotanuki.platform.jvm.core.rest

import java.time.Duration

data class HttpResilience(
    val retriesAttemptPerRequest: Int,
    val delayBetweenRetries: Duration,
    val timeoutForHttpRequest: Duration
) {

    companion object {
        private const val RETRY_ATTEMPTS_PER_REQUEST = 4
        private val HTTP_REQUEST_TIMEOUT: Duration = Duration.ofSeconds(15L)
        private val DELAY_BETWEEN_RETRIES: Duration = Duration.ofSeconds(1L)

        fun createDefault() = HttpResilience(
            RETRY_ATTEMPTS_PER_REQUEST,
            DELAY_BETWEEN_RETRIES,
            HTTP_REQUEST_TIMEOUT
        )
    }
}
