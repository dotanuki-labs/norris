package io.dotanuki.features.search.data

import com.google.common.truth.Truth.assertThat
import io.dotanuki.features.search.domain.SearchOptions
import io.dotanuki.platform.android.testing.persistance.StorageTestHelper
import io.dotanuki.platform.jvm.testing.rest.RestScenario
import io.dotanuki.platform.jvm.testing.rest.RestTestHelper
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [32])
class SearchDataSourceTests {

    private val restTestHelper = RestTestHelper()
    private val storageTestHelper = StorageTestHelper()
    private val dataSource = SearchesDataSource(storageTestHelper.createStorage(), restTestHelper.createClient())
    private val suggestions = listOf("code", "dev", "humor")

    @Before fun `before each test`() {
        storageTestHelper.clearStorage()

        restTestHelper.defineScenario(RestScenario.Categories(suggestions))
    }

    @Test fun `should return only suggestions when history not available`() {
        val actual = runBlocking { dataSource.searchOptions() }

        val expected = SearchOptions(suggestions, emptyList())
        assertThat(actual).isEqualTo(expected)
    }

    @Test fun `should return only suggestions and history`() {
        val searches = listOf("javascript, php").onEach { storageTestHelper.registerNewSearch(it) }

        val actual = runBlocking { dataSource.searchOptions() }

        val expected = SearchOptions(suggestions, searches)
        assertThat(actual).isEqualTo(expected)
    }
}
