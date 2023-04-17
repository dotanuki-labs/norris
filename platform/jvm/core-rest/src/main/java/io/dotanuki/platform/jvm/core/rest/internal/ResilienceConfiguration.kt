package io.dotanuki.platform.jvm.core.rest.internal

import java.time.Duration

object ResilienceConfiguration {
    const val RETRY_ATTEMPTS_PER_REQUEST = 4
    val DELAY_BETWEEN_RETRIES: Duration = Duration.ofSeconds(1L)
    val HTTP_REQUEST_TIMEOUT: Duration = Duration.ofSeconds(15L)
}
