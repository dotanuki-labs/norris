package io.dotanuki.platform.jvm.testing.rest

import io.dotanuki.platform.jvm.core.rest.HttpResilience
import java.time.Duration

object FakeHttpResilience {

    fun create(): HttpResilience =
        HttpResilience(
            retriesAttemptPerRequest = 3,
            delayBetweenRetries = Duration.ofSeconds(1L),
            timeoutForHttpRequest = Duration.ofSeconds(3L)
        )
}
