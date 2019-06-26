package io.dotanuki.demos.norris.facts

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import io.dotanuki.norris.domain.ChuckNorrisDotIO
import io.dotanuki.norris.domain.RawCategories
import io.dotanuki.norris.domain.RawSearch
import kotlinx.coroutines.runBlocking

fun given(mockService: ChuckNorrisDotIO, block: ScenarioBuilder.() -> Unit) =
    ScenarioBuilder(ScenarioComposer(mockService)).apply { block() }

class ScenarioBuilder(private val composer: ScenarioComposer) {

    fun responseWillMatch(setup: Scenario.() -> Unit) =
        Scenario(composer).apply { setup() }.configure()
}

class Scenario(private val composer: ScenarioComposer) {

    lateinit var criteria: HandledCondition

    fun configure() {
        when (criteria) {
            is IssueFound -> composer.searchFailed(criteria as IssueFound)
            is SuccessfulSearch -> composer.newSearch(criteria as SuccessfulSearch)
        }
    }
}

sealed class HandledCondition
class IssueFound(val error: Throwable) : HandledCondition()
class SuccessfulSearch(val data: RawSearch) : HandledCondition()

class ScenarioComposer(private val mockService: ChuckNorrisDotIO) {

    fun searchFailed(condition: IssueFound) {
        runBlocking {
            whenever(mockService.categories()).thenAnswer {
                throw condition.error
            }

            whenever(mockService.search(any())).thenAnswer {
                throw condition.error
            }
        }
    }

    fun newSearch(result: SuccessfulSearch) {
        runBlocking {

            whenever(mockService.search(any()))
                .thenReturn(result.data)

            whenever(mockService.categories())
                .thenReturn(
                    RawCategories(listOf("dev"))
                )
        }
    }
}