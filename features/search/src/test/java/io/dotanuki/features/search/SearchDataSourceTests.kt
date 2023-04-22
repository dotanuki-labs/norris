package io.dotanuki.features.search

import com.google.common.truth.Truth.assertThat
import io.dotanuki.features.search.data.SearchesDataSource
import io.dotanuki.features.search.domain.SearchOptions
import io.dotanuki.platform.android.core.persistance.LocalStorage
import io.dotanuki.platform.android.testing.app.TestApplication
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient
import io.dotanuki.platform.jvm.testing.rest.FakeChuckNorrisService
import io.dotanuki.platform.jvm.testing.rest.FakeChuckNorrisService.Scenario
import io.dotanuki.platform.jvm.testing.rest.FakeHttpResilience
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class, sdk = [32])
class SearchDataSourceTests {

    private val service = FakeChuckNorrisService()
    private lateinit var dataSource: SearchesDataSource
    private lateinit var storage: LocalStorage

    @Before fun `before each test`() {
        val resilience = FakeHttpResilience.create()
//        storage = TODO()
        dataSource = SearchesDataSource(storage, ChuckNorrisServiceClient(service, resilience))
    }

    @Test fun `should return only suggestions when history not available`() {
        val suggestions = listOf("code", "dev", "humor")
        service.scenario = Scenario.CategoriesWithSuccess(suggestions)

        val actual = runBlocking { dataSource.searchOptions() }

        val expected = SearchOptions(suggestions, emptyList())
        assertThat(actual).isEqualTo(expected)
    }

    @Test fun `should return only suggestions and history`() {
        val suggestions = listOf("code", "dev", "humor")
        service.scenario = Scenario.CategoriesWithSuccess(suggestions)

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
