package io.dotanuki.norris.persistance.tests

import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.persistance.LocalStorage
import io.dotanuki.norris.persistance.SearchHistoryInfrastructure
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class SearchHistoryInfrastructureTests {

    private lateinit var storage: LocalStorage

    @Before fun `before each test`() {
        storage = SearchHistoryInfrastructure(FakePreferences)

        FakePreferences.run {
            brokenMode = false
            storage = ""
        }
    }

    @Test fun `should retrive empty search history`() {
        runBlocking {
            val noHistory = emptyList<String>()
            assertThat(storage.lastSearches()).isEqualTo(noHistory)
        }
    }

    @Test fun `should store new search term`() {
        runBlocking {
            storage.registerNewSearch("Norris")
            val newHistory = listOf("Norris")
            assertThat(storage.lastSearches()).isEqualTo(newHistory)
        }
    }

    @Test fun `should not persist the same term more than once`() {
        runBlocking {
            repeat(times = 3) { storage.registerNewSearch("Norris") }
            val newHistory = listOf("Norris")
            assertThat(storage.lastSearches()).isEqualTo(newHistory)
        }
    }
}
