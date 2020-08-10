package io.dotanuki.norris.domain

import io.dotanuki.norris.domain.services.SearchesHistoryService
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ManageSearchQueryTests {

    class FakeSearchesHistoryService(private val history: List<String>) : SearchesHistoryService {
        override suspend fun lastSearches(): List<String> = history

        override suspend fun registerNewSearch(term: String) = Unit
    }

    @Test fun `should return fallback query when no history available`() {
        runBlocking {
            val history = emptyList<String>()
            val service = FakeSearchesHistoryService(history)
            val manager = ManageSearchQuery(service)

            val actualQuery = manager.actualQuery()

            val expected = ManageSearchQuery.FALLBACK
            assertThat(actualQuery).isEqualTo(expected)
        }
    }

    @Test fun `should return first search query when history available`() {
        runBlocking {
            val history = listOf("Norris", "Dev", "Kotlin")
            val service = FakeSearchesHistoryService(history)
            val manager = ManageSearchQuery(service)

            val actualQuery = manager.actualQuery()

            assertThat(actualQuery).isEqualTo("Norris")
        }
    }
}