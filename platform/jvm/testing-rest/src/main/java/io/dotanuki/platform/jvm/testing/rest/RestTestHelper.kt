package io.dotanuki.platform.jvm.testing.rest

import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient

class RestTestHelper {

    private val fakeResilience = FakeHttpResilience.create()
    private val fakeService = FakeChuckNorrisService()
    private val fakeClient = ChuckNorrisServiceClient(fakeService, fakeResilience)

    fun createClient(): ChuckNorrisServiceClient = fakeClient

    fun defineScenario(target: RestScenario) {
        fakeService.scenario = target
    }
}
