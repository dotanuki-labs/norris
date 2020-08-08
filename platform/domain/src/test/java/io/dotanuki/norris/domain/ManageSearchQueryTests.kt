package io.dotanuki.norris.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.dotanuki.norris.domain.services.SearchesHistoryService
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class ManageSearchQueryTests {

    private val searchHistory = mock<SearchesHistoryService>()

    private lateinit var manager: ManageSearchQuery

    @Before fun `before each test`() {
        manager = ManageSearchQuery(searchHistory)
    }

    @Test fun `should return fallback query when no history available`() {
        runBlocking {
            whenever(searchHistory.lastSearches()).thenReturn(emptyList())
            assertThat(manager.actualQuery()).isEqualTo(ManageSearchQuery.FALLBACK)
        }
    }

    @Test fun `should return first search query when history available`() {
        runBlocking {
            val history = listOf("Norris", "Dev", "Kotlin")
            whenever(searchHistory.lastSearches()).thenReturn(history)
            assertThat(manager.actualQuery()).isEqualTo(history.first())
        }
    }
}