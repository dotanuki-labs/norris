package io.dotanuki.norris.persistance.tests

import io.dotanuki.norris.domain.errors.SearchHistoryError
import io.dotanuki.norris.domain.services.SearchesHistoryService
import io.dotanuki.norris.persistance.SearchHistoryInfrastructure
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Test

internal class SearchHistoryInfrastructureTests {

    lateinit var service: SearchesHistoryService

    @Before fun `before each test`() {
        service = SearchHistoryInfrastructure(FakePreferences)

        FakePreferences.run {
            brokenMode = false
            storage.clear()
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

    @Test fun `should report broken preferences history`() {
        FakePreferences.brokenMode = true

        assertThatThrownBy {
            runBlocking { service.lastSearches() }
        }.isEqualTo(
            SearchHistoryError
        )
    }
}