package io.dotanuki.norris.search.tests

import app.cash.turbine.test
import io.dotanuki.coroutines.testutils.CoroutinesTestHelper
import io.dotanuki.coroutines.testutils.unwrapError
import io.dotanuki.norris.architecture.CommandsProcessor
import io.dotanuki.norris.architecture.StateContainer
import io.dotanuki.norris.architecture.StateMachine
import io.dotanuki.norris.architecture.TaskExecutor
import io.dotanuki.norris.architecture.UnsupportedUserInteraction
import io.dotanuki.norris.architecture.UserInteraction
import io.dotanuki.norris.architecture.UserInteraction.OpenedScreen
import io.dotanuki.norris.architecture.ViewState.FirstLaunch
import io.dotanuki.norris.architecture.ViewState.Loading
import io.dotanuki.norris.architecture.ViewState.Success
import io.dotanuki.norris.domain.ComposeSearchOptions
import io.dotanuki.norris.domain.FetchCategories
import io.dotanuki.norris.domain.ManageSearchQuery
import io.dotanuki.norris.domain.model.ChuckNorrisFact
import io.dotanuki.norris.domain.model.RelatedCategory
import io.dotanuki.norris.domain.model.SearchOptions
import io.dotanuki.norris.domain.services.CategoriesCacheService
import io.dotanuki.norris.domain.services.RemoteFactsService
import io.dotanuki.norris.domain.services.SearchesHistoryService
import io.dotanuki.norris.search.SearchPresentation
import io.dotanuki.norris.search.SearchPresentation.QueryValidation
import io.dotanuki.norris.search.SearchPresentation.Suggestions
import io.dotanuki.norris.search.SearchViewModel
import io.dotanuki.norris.search.ValidateQuery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

class SearchViewModelTests {

    @get:Rule val helper = CoroutinesTestHelper()

    private lateinit var viewModel: SearchViewModel

    class FakeSearchesHistoryService : SearchesHistoryService {
        override suspend fun lastSearches(): List<String> = listOf("Code")

        override suspend fun registerNewSearch(term: String) = Unit
    }

    class FakeCategoriesCacheService : CategoriesCacheService {
        override fun save(categories: List<RelatedCategory.Available>) = Unit

        override fun cached(): List<RelatedCategory.Available>? = listOf(
            RelatedCategory.Available("dev"),
            RelatedCategory.Available("humor")
        )
    }

    class FakeRemoteFactsService : RemoteFactsService {
        override suspend fun availableCategories(): List<RelatedCategory.Available> =
            listOf(
                RelatedCategory.Available("dev"),
                RelatedCategory.Available("humor"),
                RelatedCategory.Available("soccer")
            )

        override suspend fun fetchFacts(searchTerm: String): List<ChuckNorrisFact> = emptyList()
    }

    @Before fun `before each test`() {

        val taskExecutor = TaskExecutor.Synchronous(helper.scope)

        val machine =
            StateMachine<SearchPresentation>(
                executor = taskExecutor,
                container = StateContainer.Unbounded(helper.scope)
            )

        val processor = CommandsProcessor(taskExecutor)

        val manageQuery = ManageSearchQuery(FakeSearchesHistoryService())
        val fetchCategories = FetchCategories(FakeCategoriesCacheService(), FakeRemoteFactsService())

        val composeOptions = ComposeSearchOptions(FakeSearchesHistoryService(), fetchCategories)

        viewModel = SearchViewModel(composeOptions, manageQuery, processor, machine)
    }

    @Test fun `should report unsupported interaction`() {

        val result = runCatching {
            viewModel.handle(UserInteraction.RequestedFreshContent)
        }

        assertThat(unwrapError(result)).isEqualTo(UnsupportedUserInteraction)
    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @Test fun `should display suggestions`() {
        runBlocking {
            viewModel.run {
                bindToStates().test {
                    handle(OpenedScreen)

                    val options = SearchOptions(
                        history = listOf("Code"),
                        recommendations = listOf("dev", "humor")
                    )

                    val emissions = listOf(expectItem(), expectItem(), expectItem())

                    val viewStates = listOf(
                        FirstLaunch,
                        Loading.FromEmpty,
                        Success(Suggestions(options))
                    )

                    assertThat(emissions).isEqualTo(viewStates)
                }
            }
        }
    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @Test fun `should validate incoming query`() {
        runBlocking {
            viewModel.run {
                bindToStates().test {
                    handle(ValidateQuery("Norris"))

                    val emissions = listOf(expectItem(), expectItem(), expectItem())

                    val viewStates = listOf(
                        FirstLaunch,
                        Loading.FromEmpty,
                        Success(QueryValidation(true))
                    )

                    assertThat(emissions).isEqualTo(viewStates)
                }
            }
        }
    }
}