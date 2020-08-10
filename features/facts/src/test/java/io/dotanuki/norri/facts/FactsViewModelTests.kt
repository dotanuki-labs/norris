package io.dotanuki.norri.facts

import app.cash.turbine.test
import io.dotanuki.coroutines.testutils.CoroutinesTestHelper
import io.dotanuki.norris.architecture.StateContainer
import io.dotanuki.norris.architecture.StateMachine
import io.dotanuki.norris.architecture.TaskExecutor
import io.dotanuki.norris.architecture.UserInteraction
import io.dotanuki.norris.architecture.ViewState.Failed
import io.dotanuki.norris.architecture.ViewState.FirstLaunch
import io.dotanuki.norris.architecture.ViewState.Loading
import io.dotanuki.norris.architecture.ViewState.Success
import io.dotanuki.norris.domain.FetchFacts
import io.dotanuki.norris.domain.ManageSearchQuery
import io.dotanuki.norris.domain.errors.RemoteServiceIntegrationError.UnexpectedResponse
import io.dotanuki.norris.domain.model.ChuckNorrisFact
import io.dotanuki.norris.domain.model.RelatedCategory
import io.dotanuki.norris.domain.services.RemoteFactsService
import io.dotanuki.norris.domain.services.SearchesHistoryService
import io.dotanuki.norris.facts.FactDisplayRow
import io.dotanuki.norris.facts.FactsPresentation
import io.dotanuki.norris.facts.FactsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

class FactsViewModelTests {

    @get:Rule val helper = CoroutinesTestHelper()

    private lateinit var viewModel: FactsViewModel
    private lateinit var factsService: FakeRemoteFactsServices

    class FakeRemoteFactsServices(var withResults: Boolean = true) : RemoteFactsService {

        private val facts by lazy {
            listOf(
                ChuckNorrisFact(
                    id = "2wzginmks8azrbaxnamxdw",
                    shareableUrl = "https://api.chucknorris.io/jokes/2wzginmks8azrbaxnamxdw",
                    textual = "Chuck Norris commits before Git repo even exits",
                    category = RelatedCategory.Available("dev")
                )
            )
        }

        private val error by lazy {
            UnexpectedResponse
        }

        private val categories by lazy {
            listOf(
                RelatedCategory.Available("dev")
            )
        }

        override suspend fun availableCategories(): List<RelatedCategory.Available> = categories

        override suspend fun fetchFacts(searchTerm: String): List<ChuckNorrisFact> =
            if (withResults) facts else throw error
    }

    @Before fun `before each test`() {
        val stateMachine = StateMachine<FactsPresentation>(
            executor = TaskExecutor.Synchronous(helper.scope),
            container = StateContainer.Unbounded(helper.scope)
        )

        factsService = FakeRemoteFactsServices()
        val fetchFacts = FetchFacts(factsService)

        val historyService = object : SearchesHistoryService {
            override suspend fun lastSearches(): List<String> = emptyList()

            override suspend fun registerNewSearch(term: String) = Unit
        }

        val manageQuery = ManageSearchQuery(historyService)
        viewModel = FactsViewModel(fetchFacts, manageQuery, stateMachine)
    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @Test fun `should report failure when fetching from remote`() {
        runBlocking {

            factsService.withResults = false

            viewModel.run {
                bind().test {
                    handle(UserInteraction.OpenedScreen)

                    val emissions = listOf(expectItem(), expectItem(), expectItem())
                    val viewStates = listOf(FirstLaunch, Loading.FromEmpty, Failed(UnexpectedResponse))

                    assertThat(emissions).isEqualTo(viewStates)
                }
            }
        }
    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @Test fun `should fetch facts from remote data source with success`() {
        runBlocking {
            viewModel.run {
                bind().test {
                    handle(UserInteraction.OpenedScreen)

                    val presentation = FactsPresentation(
                        ManageSearchQuery.FALLBACK,
                        listOf(
                            FactDisplayRow(
                                tag = RelatedCategory.Available("dev"),
                                url = "https://api.chucknorris.io/jokes/2wzginmks8azrbaxnamxdw",
                                fact = "Chuck Norris commits before Git repo even exits",
                                displayWithSmallerFontSize = false
                            )
                        )
                    )

                    val emissions = listOf(expectItem(), expectItem(), expectItem())
                    val viewStates = listOf(FirstLaunch, Loading.FromEmpty, Success(presentation))

                    assertThat(emissions).isEqualTo(viewStates)
                }
            }
        }
    }
}