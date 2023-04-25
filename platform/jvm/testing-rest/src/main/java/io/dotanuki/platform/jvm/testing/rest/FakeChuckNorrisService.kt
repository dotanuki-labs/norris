package io.dotanuki.platform.jvm.testing.rest

import io.dotanuki.platform.jvm.core.rest.ChuckNorrisService
import io.dotanuki.platform.jvm.core.rest.RawSearch

internal class FakeChuckNorrisService : ChuckNorrisService {

    var scenario: RestScenario = RestScenario.NotDefined

    override suspend fun categories(): List<String> =
        when (val target = scenario) {
            is RestScenario.Categories -> target.results
            is RestScenario.Error -> throw target.error
            else -> error("Scenario does not apply for 'categories'")
        }

    override suspend fun search(query: String): RawSearch =
        when (val target = scenario) {
            is RestScenario.Facts -> target.result
            is RestScenario.Error -> throw target.error
            else -> error("Scenario does not apply for 'search'")
        }
}
