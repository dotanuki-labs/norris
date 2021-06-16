package io.dotanuki.norris.domain

import com.google.common.truth.Truth.assertAbout
import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.domain.errors.SearchFactsError
import io.dotanuki.norris.domain.model.ChuckNorrisFact
import io.dotanuki.norris.domain.model.RelatedCategory.Available
import io.dotanuki.norris.domain.services.RemoteFactsService
import io.dotanuki.testing.truth.EspeculativeExecution
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class FetchFactsTests {

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
        listOf(Available("dev"))
    }

    class FakeRemoteFactsService(
        private val fakeFacts: List<ChuckNorrisFact>,
        private val fakeCategories: List<Available>
    ) : RemoteFactsService {

        override suspend fun availableCategories(): List<Available> = fakeCategories

        override suspend fun fetchFacts(searchTerm: String): List<ChuckNorrisFact> =
            when (searchTerm) {
                "Norris" -> fakeFacts
                else -> emptyList()
            }
    }

    @Before fun `before each test`() {
        val service = FakeRemoteFactsService(facts, categories)
        usecase = FetchFacts(service)
    }

    @Test fun `should fetch valid term`() {
        runBlocking {
            val searched = usecase.search("Norris")
            assertThat(searched).isEqualTo(facts)
        }
    }

    @Test fun `should throw with invalid term`() {
        val execution = EspeculativeExecution { runBlocking { usecase.search("") } }

        val expectedError = SearchFactsError.EmptyTerm

        assertAbout(execution).that(expectedError).hasBeingThrown()
    }

    @Test fun `should throw with empty result`() {
        val execution = EspeculativeExecution { runBlocking { usecase.search("Trump") } }

        val expectedError = SearchFactsError.NoResultsFound

        assertAbout(execution).that(expectedError).hasBeingThrown()
    }
}
