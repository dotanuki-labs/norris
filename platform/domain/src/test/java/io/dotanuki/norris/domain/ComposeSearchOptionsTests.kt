package io.dotanuki.norris.domain

import io.dotanuki.norris.domain.model.ChuckNorrisFact
import io.dotanuki.norris.domain.model.RelatedCategory.Available
import io.dotanuki.norris.domain.model.SearchOptions
import io.dotanuki.norris.domain.services.CategoriesCacheService
import io.dotanuki.norris.domain.services.RemoteFactsService
import io.dotanuki.norris.domain.services.SearchesHistoryService
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ComposeSearchOptionsTests {

    @Test fun `should compose options with categories and search history`() {
        runBlocking {

            // Given
            val categories = listOf(Available("dev"), Available("humor"), Available("soccer"))

            val pastSearches = listOf("Obama", "Brazil")

            val searchHistory = object : SearchesHistoryService {
                override suspend fun lastSearches(): List<String> = pastSearches

                override suspend fun registerNewSearch(term: String) = Unit
            }

            val categoriesCache = object : CategoriesCacheService {
                override fun save(categories: List<Available>) = Unit

                override fun cached(): List<Available>? = categories
            }

            val remoteFacts = object : RemoteFactsService {
                override suspend fun availableCategories(): List<Available> = categories

                override suspend fun fetchFacts(searchTerm: String): List<ChuckNorrisFact> = emptyList()
            }

            val fetchCategories = FetchCategories(categoriesCache, remoteFacts)
            val usecase = ComposeSearchOptions(searchHistory, fetchCategories)

            // When
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