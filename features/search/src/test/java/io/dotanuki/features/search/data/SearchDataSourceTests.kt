package io.dotanuki.features.search.data

import com.google.common.truth.Truth.assertThat
import io.dotanuki.features.search.domain.SearchOptions
import io.dotanuki.platform.android.testing.app.TestApplication
import io.dotanuki.platform.android.testing.persistance.PersistanceHelper
import io.dotanuki.platform.jvm.testing.rest.RestScenario
import io.dotanuki.platform.jvm.testing.rest.RestTestHelper
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class, sdk = [32])
class SearchDataSourceTests {

    private val restTestHelper = RestTestHelper()
    private val localStorage = PersistanceHelper.storage
    private val dataSource = SearchesDataSource(localStorage, restTestHelper.createClient())
    private val suggestions = listOf("code", "dev", "humor")

    @Before fun `before each test`() {
        PersistanceHelper.clearStorage()

        restTestHelper.defineScenario(RestScenario.Categories(suggestions))
    }

    @Test fun `should return only suggestions when history not available`() {
        val actual = runBlocking { dataSource.searchOptions() }

        val expected = SearchOptions(suggestions, emptyList())
        assertThat(actual).isEqualTo(expected)
    }

    @Test fun `should return only suggestions and history`() {
        val searches = listOf("javascript, php").onEach { localStorage.registerNewSearch(it) }

        val actual = runBlocking { dataSource.searchOptions() }

        val expected = SearchOptions(suggestions, searches)
        assertThat(actual).isEqualTo(expected)
    }

    @Test fun `should save a new search`() {
        runBlocking {
            val newSearch = "kotlin"
            dataSource.saveNewSearch(newSearch)

            val history = localStorage.lastSearches()

            assertThat(history).contains(newSearch)
        }
    }
}
