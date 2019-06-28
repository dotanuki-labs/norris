package io.dotanuki.norris.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.dotanuki.norris.domain.model.RelatedCategory.Available
import io.dotanuki.norris.domain.model.SearchOptions
import io.dotanuki.norris.domain.services.RemoteFactsService
import io.dotanuki.norris.domain.services.SearchesHistoryService
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ComposeSearchOptionsTests {

    private val factsService = mock<RemoteFactsService>()
    private val searchHistory = mock<SearchesHistoryService>()

    private val categories by lazy {
        listOf(
            Available("dev"),
            Available("humor"),
            Available("soccer")
        )
    }

    private val pastSearches by lazy {
        listOf(
            "Obama",
            "Brazil"
        )
    }

    @Test fun `should compose options with categories and search history`() {

        runBlocking {

            // Given

            val usecase = ComposeSearchOptions(searchHistory, factsService)

            // When
            whenever(factsService.availableCategories()).thenReturn(categories)
            whenever(searchHistory.lastSearches()).thenReturn(pastSearches)

            // And

            val combined = usecase.execute()
            // Then

            val expected = SearchOptions(
                history = pastSearches,
                recommendations = listOf("dev", "humor", "soccer")
            )

            assertThat(combined).isEqualTo(expected)
        }
    }
}