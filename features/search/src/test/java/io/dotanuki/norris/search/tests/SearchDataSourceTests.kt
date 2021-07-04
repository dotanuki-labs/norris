package io.dotanuki.norris.search.tests

import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.persistance.LocalStorage
import io.dotanuki.norris.search.data.SearchesDataSource
import io.dotanuki.norris.search.di.searchModule
import io.dotanuki.norris.search.domain.SearchOptions
import io.dotanuki.testing.app.TestApplication
import io.dotanuki.testing.rest.RestDataBuilder
import io.dotanuki.testing.rest.RestInfrastructureRule
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchDataSourceTests {

    @get:Rule val rule = RestInfrastructureRule()

    private lateinit var dataSource: SearchesDataSource
    private lateinit var storage: LocalStorage

    @Before fun `before each test`() {
        val testApplication = TestApplication.setupWith(searchModule)
        storage = testApplication.localStorage
        dataSource = SearchesDataSource(storage, rule.api)
    }

    @Test fun `should return only suggestions when history not available`() {
        val suggestions = listOf("code", "dev", "humor")
        val payload = RestDataBuilder.suggestionsPayload(suggestions)
        rule.defineScenario(200, payload)

        val actual = runBlocking { dataSource.searchOptions() }

        val expected = SearchOptions(suggestions, emptyList())
        assertThat(actual).isEqualTo(expected)
    }

    @Test fun `should return only suggestions and history`() {
        val suggestions = listOf("code", "dev", "humor")
        val payload = RestDataBuilder.suggestionsPayload(suggestions)
        rule.defineScenario(200, payload)

        val searches = listOf("javascript, php").onEach { storage.registerNewSearch(it) }

        val actual = runBlocking { dataSource.searchOptions() }

        val expected = SearchOptions(suggestions, searches)
        assertThat(actual).isEqualTo(expected)
    }

    @Test fun `should save a new search`() {
        runBlocking {
            val newSearch = "kotlin"
            dataSource.saveNewSearch(newSearch)

            val history = storage.lastSearches()

            assertThat(history).contains(newSearch)
        }
    }
}
