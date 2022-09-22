package io.dotanuki.platform.android.core.persistance

import com.google.common.truth.Truth.assertThat
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
