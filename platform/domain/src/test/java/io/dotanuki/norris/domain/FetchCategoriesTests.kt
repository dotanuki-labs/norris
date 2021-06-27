package io.dotanuki.norris.domain

import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.domain.model.ChuckNorrisFact
import io.dotanuki.norris.domain.model.RelatedCategory
import io.dotanuki.norris.domain.services.CategoriesCacheService
import io.dotanuki.norris.domain.services.RemoteFactsService
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.IOException

class FetchCategoriesTests {

    private lateinit var categoriesCache: CategoriesCacheService
    private lateinit var remoteFacts: RemoteFactsService

    private val categories = mutableListOf(
        RelatedCategory.Available("dev"),
        RelatedCategory.Available("code")
    )

    @Before fun `before each test`() {
    }

    @Test fun `should fetch from cache from available`() {
        runBlocking {
            `given that remote service not available`()
            `given that cache returns categories`()

            val usecase = FetchCategories(categoriesCache, remoteFacts)

            assertThat(usecase.execute()).isEqualTo(categories)
        }
    }

    @Test fun `should fetch from remote when cache absent`() {
        runBlocking {
            `given that remote service returns categories`()
            `given that cache is absent`()

            val usecase = FetchCategories(categoriesCache, remoteFacts)

            assertThat(usecase.execute()).isEqualTo(categories)
        }
    }

    private fun `given that cache is absent`() {
        categoriesCache = object : CategoriesCacheService {
            override fun save(categories: List<RelatedCategory.Available>) = Unit

            override fun cached(): List<RelatedCategory.Available>? = null
        }
    }

    private fun `given that cache returns categories`() {
        categoriesCache = object : CategoriesCacheService {
            override fun save(categories: List<RelatedCategory.Available>) = Unit

            override fun cached(): List<RelatedCategory.Available>? = categories
        }
    }

    private fun `given that remote service returns categories`() {
        remoteFacts = object : RemoteFactsService {
            override suspend fun availableCategories(): List<RelatedCategory.Available> = categories

            override suspend fun fetchFacts(searchTerm: String): List<ChuckNorrisFact> = emptyList()
        }
    }

    private fun `given that remote service not available`() {
        remoteFacts = object : RemoteFactsService {
            override suspend fun availableCategories(): List<RelatedCategory.Available> {
                throw IOException("Timeout")
            }

            override suspend fun fetchFacts(searchTerm: String): List<ChuckNorrisFact> = emptyList()
        }
    }
}
