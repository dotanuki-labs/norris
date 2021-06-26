package io.dotanuki.norris.persistance.tests

import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.domain.services.SearchesHistoryService
import io.dotanuki.norris.persistance.SearchHistoryInfrastructure
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class SearchHistoryInfrastructureTests {

    private lateinit var service: SearchesHistoryService

    @Before fun `before each test`() {
        service = SearchHistoryInfrastructure(FakePreferences)

        FakePreferences.run {
            brokenMode = false
            storage = ""
        }
    }

    @Test fun `should retrive empty search history`() {
        runBlocking {
            val noHistory = emptyList<String>()
            assertThat(service.lastSearches()).isEqualTo(noHistory)
        }
    }

    @Test fun `should store new search term`() {
        runBlocking {
            service.registerNewSearch("Norris")
            val newHistory = listOf("Norris")
            assertThat(service.lastSearches()).isEqualTo(newHistory)
        }
    }

    @Test fun `should not persist the same term more than once`() {
        runBlocking {
            repeat(times = 3) { service.registerNewSearch("Norris") }
            val newHistory = listOf("Norris")
            assertThat(service.lastSearches()).isEqualTo(newHistory)
        }
    }
}
