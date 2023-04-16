package io.dotanuki.platform.jvm.testing.rest

import io.dotanuki.platform.jvm.core.rest.ChuckNorrisService
import io.dotanuki.platform.jvm.core.rest.RawSearch

class FakeChuckNorrisService : ChuckNorrisService {

    var scenario: Scenario = Scenario.NotDefined

    sealed class Scenario {
        object NotDefined : Scenario()
        data class CategoriesWithSuccess(val results: List<String>) : Scenario()
        data class CategoriesWithError(val error: Throwable) : Scenario()
        data class FactsWithSuccess(val result: RawSearch) : Scenario()
        data class FactsWithError(val error: Throwable) : Scenario()
    }

    override suspend fun categories(): List<String> =
        when (val target = scenario) {
            is Scenario.CategoriesWithSuccess -> target.results
            is Scenario.CategoriesWithError -> throw target.error
            else -> error("Scenario does not apply for 'categories'")
        }

    override suspend fun search(query: String): RawSearch =
        when (val target = scenario) {
            is Scenario.FactsWithSuccess -> target.result
            is Scenario.FactsWithError -> throw target.error
            else -> error("Scenario does not apply for 'search'")
        }
}
