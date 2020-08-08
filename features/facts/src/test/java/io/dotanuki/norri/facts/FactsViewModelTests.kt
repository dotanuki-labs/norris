package io.dotanuki.norri.facts

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.dotanuki.coroutines.testutils.CoroutinesTestHelper
import io.dotanuki.coroutines.testutils.FlowTest.Companion.flowTest
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
import io.dotanuki.norris.facts.FactDisplayRow
import io.dotanuki.norris.facts.FactsPresentation
import io.dotanuki.norris.facts.FactsViewModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class FactsViewModelTests {

    @get:Rule val helper = CoroutinesTestHelper()

    private val fetchFacts = mock<FetchFacts>()
    private val manageQuery = mock<ManageSearchQuery>()

    private lateinit var viewModel: FactsViewModel

    @Before fun `before each test`() {
        val stateMachine = StateMachine<FactsPresentation>(
            executor = TaskExecutor.Synchronous(helper.scope),
            container = StateContainer.Unbounded(helper.scope)
        )

        viewModel = FactsViewModel(fetchFacts, manageQuery, stateMachine)
    }

    @Ignore @Test fun `should report failure when fetching from remote`() {

        // Given
        flowTest(viewModel.bind()) {

            triggerEmissions {

                // When
                whenever(fetchFacts.search(anyString()))
                    .thenAnswer { throw UnexpectedResponse }

                // And
                viewModel.handle(UserInteraction.OpenedScreen)
            }

            afterCollect { emissions ->

                // Then
                val viewStates = listOf(
                    FirstLaunch,
                    Loading.FromEmpty,
                    Failed(UnexpectedResponse)
                )

                assertThat(emissions).isEqualTo(viewStates)
            }
        }
    }

    @Ignore @Test fun `should fetch article from remote data source with success`() {

        // Given

        val categories = listOf(
            RelatedCategory.Available("dev")
        )

        val facts = listOf(
            ChuckNorrisFact(
                id = "2wzginmks8azrbaxnamxdw",
                shareableUrl = "https://api.chucknorris.io/jokes/2wzginmks8azrbaxnamxdw",
                textual = "Chuck Norris commits before Git repo even exits",
                category = RelatedCategory.Available("dev")
            )
        )

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

        flowTest(viewModel.bind()) {

            triggerEmissions {

                // When
                whenever(fetchFacts.search(anyString())).thenReturn(facts)

                // And
                viewModel.handle(UserInteraction.OpenedScreen)
            }

            afterCollect { emissions ->

                // Then
                val viewStates = listOf(
                    FirstLaunch,
                    Loading.FromEmpty,
                    Success(presentation)
                )

                assertThat(emissions).isEqualTo(viewStates)
            }
        }
    }
}