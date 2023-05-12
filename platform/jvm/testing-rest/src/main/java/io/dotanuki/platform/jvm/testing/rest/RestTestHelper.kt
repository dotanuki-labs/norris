package io.dotanuki.platform.jvm.testing.rest

import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient
import io.dotanuki.platform.jvm.core.rest.HttpResilience
import java.time.Duration

class RestTestHelper {

    private val testResilienceSpec by lazy {
        HttpResilience(
            retriesAttemptPerRequest = 3,
            delayBetweenRetries = Duration.ofSeconds(1L),
            timeoutForHttpRequest = Duration.ofSeconds(3L)
        )
    }

    private val fakeService by lazy {
        FakeChuckNorrisService()
    }

    val restClient by lazy {
        ChuckNorrisServiceClient(fakeService, testResilienceSpec)
    }

    fun defineScenario(target: RestScenario) {
        fakeService.scenario = target
    }
}
