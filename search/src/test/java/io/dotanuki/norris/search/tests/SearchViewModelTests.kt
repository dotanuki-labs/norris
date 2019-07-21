package io.dotanuki.norris.search.tests

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.dotanuki.coroutines.testutils.CoroutinesTestHelper
import io.dotanuki.coroutines.testutils.FlowTest.Companion.flowTest
import io.dotanuki.coroutines.testutils.unwrapError
import io.dotanuki.norris.architecture.CommandsProcessor
import io.dotanuki.norris.architecture.StateContainer
import io.dotanuki.norris.architecture.StateMachine
import io.dotanuki.norris.architecture.TaskExecutor
import io.dotanuki.norris.architecture.UnsupportedUserInteraction
import io.dotanuki.norris.architecture.UserInteraction
import io.dotanuki.norris.architecture.UserInteraction.OpenedScreen
import io.dotanuki.norris.architecture.ViewState
import io.dotanuki.norris.architecture.ViewState.FirstLaunch
import io.dotanuki.norris.architecture.ViewState.Loading
import io.dotanuki.norris.architecture.ViewState.Success
import io.dotanuki.norris.domain.ComposeSearchOptions
import io.dotanuki.norris.domain.ManageSearchQuery
import io.dotanuki.norris.domain.errors.NetworkingError.OperationTimeout
import io.dotanuki.norris.domain.model.SearchOptions
import io.dotanuki.norris.search.QueryDefined
import io.dotanuki.norris.search.ReturnFromSearch
import io.dotanuki.norris.search.SearchPresentation
import io.dotanuki.norris.search.SearchPresentation.QueryValidation
import io.dotanuki.norris.search.SearchPresentation.Suggestions
import io.dotanuki.norris.search.SearchViewModel
import io.dotanuki.norris.search.ValidateQuery
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchViewModelTests {

    @get:Rule val helper = CoroutinesTestHelper()

    private val composeOptions = mock<ComposeSearchOptions>()
    private val manageQuery = mock<ManageSearchQuery>()

    private lateinit var viewModel: SearchViewModel

    init {
        runBlocking {
            whenever(manageQuery.actualQuery()).thenReturn("Code")
            whenever(manageQuery.save(any())).thenAnswer { Unit }
        }
    }

    @Before fun `before each test`() {

        val taskExecutor = TaskExecutor.Synchronous(helper.scope)

        val machine =
            StateMachine<SearchPresentation>(
                executor = taskExecutor,
                container = StateContainer.Unbounded(helper.scope)
            )

        val processor = CommandsProcessor(taskExecutor)

        viewModel = SearchViewModel(composeOptions, manageQuery, processor, machine)
    }

    @Test fun `should report failure when fetching from remote`() {

        // Given
        flowTest(viewModel.bindToStates()) {

            triggerEmissions {

                // When
                whenever(composeOptions.execute())
                    .thenAnswer { throw OperationTimeout }

                // And
                viewModel.handle(OpenedScreen)
            }

            afterCollect { emissions ->

                // Then
                val viewStates = listOf(
                    FirstLaunch,
                    Loading.FromEmpty,
                    ViewState.Failed(OperationTimeout)
                )

                assertThat(emissions).isEqualTo(viewStates)
            }
        }
    }

    @Test fun `should report unsupported interaction`() {

        val result = runCatching {
            viewModel.handle(UserInteraction.RequestedFreshContent)
        }

        assertThat(unwrapError(result)).isEqualTo(UnsupportedUserInteraction)
    }

    @Test fun `should display suggestions`() {

        // Given
        val options = SearchOptions(
            history = emptyList(),
            recommendations = listOf("dev", "humor", "money")
        )

        flowTest(viewModel.bindToStates()) {

            triggerEmissions {

                // When
                whenever(composeOptions.execute()).thenReturn(options)

                // And
                viewModel.handle(OpenedScreen)
            }

            afterCollect { emissions ->

                // Then
                val viewStates = listOf(
                    FirstLaunch,
                    Loading.FromEmpty,
                    Success(
                        Suggestions(options)
                    )
                )

                assertThat(emissions).isEqualTo(viewStates)
            }
        }
    }

    @Test fun `should validate incoming query`() {

        flowTest(viewModel.bindToStates()) {

            triggerEmissions {

                // When
                viewModel.handle(ValidateQuery("Norris"))

                // And
                viewModel.handle(OpenedScreen)
            }

            afterCollect { emissions ->

                // Then
                val viewStates = listOf(
                    FirstLaunch,
                    Loading.FromEmpty,
                    Success(
                        QueryValidation(true)
                    )
                )

                assertThat(emissions).isEqualTo(viewStates)
            }
        }
    }

    @Test fun `should proceed with selected query`() {

        // Given
        flowTest(viewModel.bindToCommands()) {

            triggerEmissions {

                // When
                viewModel.handle(QueryDefined("Norris"))
            }

            afterCollect { emissions ->

                // Then
                val commands = listOf(ReturnFromSearch)
                assertThat(emissions).isEqualTo(commands)
            }
        }
    }
}