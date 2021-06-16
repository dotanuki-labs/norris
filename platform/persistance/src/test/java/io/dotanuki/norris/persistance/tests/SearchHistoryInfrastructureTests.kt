package io.dotanuki.norris.persistance.tests

import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.domain.errors.SearchHistoryError
import io.dotanuki.norris.domain.services.SearchesHistoryService
import io.dotanuki.norris.persistance.SearchHistoryInfrastructure
import io.dotanuki.testing.coroutines.SuspendableErrorChecker.Companion.errorOnSuspendable
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class SearchHistoryInfrastructureTests {

    private lateinit var service: SearchesHistoryService

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

        errorOnSuspendable<List<String>> {
            take {
                FakePreferences.brokenMode = true
                emptyList()
            }

            once {
                service.lastSearches()
            }

            check { error ->
                assertThat(error).isEqualTo(SearchHistoryError)
            }
        }
    }
}
