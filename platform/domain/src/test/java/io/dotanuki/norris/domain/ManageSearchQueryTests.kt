package io.dotanuki.norris.domain

import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.domain.services.SearchesHistoryService
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ManageSearchQueryTests {

    class FakeSearchesHistoryService(private val history: List<String>) : SearchesHistoryService {
        override suspend fun lastSearches(): List<String> = history

        override fun registerNewSearch(term: String) = Unit
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

    @Test fun `should return last search query when history available`() {
        runBlocking {
            val history = listOf("Norris", "Dev", "Kotlin")
            val service = FakeSearchesHistoryService(history)
            val manager = ManageSearchQuery(service)

            val actualQuery = manager.actualQuery()

            assertThat(actualQuery).isEqualTo("Kotlin")
        }
    }
}
