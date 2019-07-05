package io.dotanuki.norris.domain

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.dotanuki.coroutines.testutils.SuspendableErrorChecker.Companion.errorOnSuspendable
import io.dotanuki.norris.domain.errors.SearchFactsError
import io.dotanuki.norris.domain.model.ChuckNorrisFact
import io.dotanuki.norris.domain.model.RelatedCategory.Available
import io.dotanuki.norris.domain.services.RemoteFactsService
import io.dotanuki.norris.domain.services.SearchesHistoryService
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class FetchFactsTests {

    private val factsService = mock<RemoteFactsService>()
    private val searchHistory = mock<SearchesHistoryService>()
    private lateinit var usecase: FetchFacts

    private val facts by lazy {
        listOf(
            ChuckNorrisFact(
                id = "2wzginmks8azrbaxnamxdw",
                shareableUrl = "https://api.chucknorris.io/jokes/2wzginmks8azrbaxnamxdw",
                textual = "Chuck Norris coded this app in Eclipse",
                category = Available("dev")
            )
        )
    }

    private val categories by lazy {
        listOf(
            Available("dev"),
            Available("humor")
        )
    }

    @Before fun `before each test`() {
        usecase = FetchFacts(factsService, searchHistory)

        runBlocking {
            whenever(factsService.availableCategories()).thenReturn(categories)
            whenever(factsService.fetchFacts(anyString())).thenReturn(facts)
        }
    }

    @Test fun `should fetch random term`() {
        runBlocking {
            assertThat(usecase.randomFacts()).isEqualTo(facts)
        }
    }

    @Test fun `should fetch valid term`() {
        runBlocking {
            assertThat(usecase.search("Trump")).isEqualTo(facts)

            argumentCaptor<String>().apply {
                verify(searchHistory).registerNewSearch(capture())
                assertThat(firstValue).isEqualTo("Trump")
            }
        }
    }

    @Test fun `should throw with invalid term`() {
        errorOnSuspendable<List<ChuckNorrisFact>> {
            take {
                facts
            }

            once {
                usecase.search("")
            }

            check { error ->
                assertThat(error).isEqualTo(SearchFactsError.EmptyTerm)
            }
        }
    }

    @Test fun `should throw with empty result`() {

        errorOnSuspendable<List<ChuckNorrisFact>> {
            take {
                emptyList()
            }

            once { facts ->
                whenever(factsService.fetchFacts(anyString())).thenReturn(facts)
                usecase.search("Norris")
            }

            check { error ->
                assertThat(error).isEqualTo(SearchFactsError.NoResultsFound)
            }
        }
    }
}