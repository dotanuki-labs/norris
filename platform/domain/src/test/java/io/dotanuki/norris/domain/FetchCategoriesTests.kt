package io.dotanuki.norris.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.dotanuki.norris.domain.errors.NetworkingError
import io.dotanuki.norris.domain.model.RelatedCategory
import io.dotanuki.norris.domain.services.CategoriesCacheService
import io.dotanuki.norris.domain.services.RemoteFactsService
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class FetchCategoriesTests {

    private val categoriesCache = mock<CategoriesCacheService>()
    private val remoteFacts = mock<RemoteFactsService>()
    private lateinit var usecase: FetchCategories

    private val categories = mutableListOf(
        RelatedCategory.Available("dev"),
        RelatedCategory.Available("code")
    )

    @Before fun `before each test`() {
        usecase = FetchCategories(categoriesCache, remoteFacts)
    }

    @Test fun `should fetch from cache from available`() {
        runBlocking {
            `given that remote service not available`()
            `given that cache returns categories`()

            assertThat(usecase.execute()).isEqualTo(categories)
        }
    }

    @Test fun `should fetch from remote when cache absent`() {
        runBlocking {
            `given that remote service returns categories`()
            `given that cache is absent`()

            assertThat(usecase.execute()).isEqualTo(categories)
        }
    }

    private fun `given that cache is absent`() {
        whenever(categoriesCache.cached()).thenReturn(null)
    }

    private suspend fun `given that remote service returns categories`() {
        whenever(remoteFacts.availableCategories()).thenReturn(categories)
    }

    private fun `given that cache returns categories`() {
        whenever(categoriesCache.cached()).thenReturn(categories)
    }

    private suspend fun `given that remote service not available`() {
        whenever(remoteFacts.availableCategories()).thenAnswer {
            throw NetworkingError.HostUnreachable
        }
    }
}