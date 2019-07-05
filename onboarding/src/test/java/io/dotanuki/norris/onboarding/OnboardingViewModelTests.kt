package io.dotanuki.norris.onboarding

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.dotanuki.coroutines.testutils.CoroutinesTestHelper
import io.dotanuki.coroutines.testutils.collectForTesting
import io.dotanuki.coroutines.testutils.unwrapError
import io.dotanuki.norris.architecture.StateContainer
import io.dotanuki.norris.architecture.StateMachine
import io.dotanuki.norris.architecture.TaskExecutor
import io.dotanuki.norris.architecture.UnsupportedUserInteraction
import io.dotanuki.norris.architecture.UserInteraction.OpenedScreen
import io.dotanuki.norris.architecture.UserInteraction.RequestedFreshContent
import io.dotanuki.norris.architecture.ViewState.FirstLaunch
import io.dotanuki.norris.architecture.ViewState.Loading
import io.dotanuki.norris.architecture.ViewState.Success
import io.dotanuki.norris.domain.FetchCategories
import io.dotanuki.norris.domain.model.RelatedCategory.Available
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class OnboardingViewModelTests {

    @get:Rule val helper = CoroutinesTestHelper()

    private val usecase = mock<FetchCategories>()

    private lateinit var viewModel: OnboardingViewModel

    private val categories =
        mutableListOf(
            Available("dev"),
            Available("code"),
            Available("politics"),
            Available("humor")
        )

    @Before fun `before each test`() {
        val stateMachine = StateMachine<Unit>(
            executor = TaskExecutor.Synchronous(helper.scope),
            container = StateContainer.Unbounded(helper.scope)
        )

        viewModel = OnboardingViewModel(usecase, stateMachine)
    }

    @Test fun `should fetch categories when opening screen`() {
        runBlocking {

            // Given
            val emissions = viewModel.bind().collectForTesting()

            // When

            whenever(usecase.execute()).thenReturn(categories)

            // And
            viewModel.handle(OpenedScreen).join()

            // Then
            val viewStates = listOf(
                FirstLaunch,
                Loading.FromEmpty,
                Success(Unit)
            )

            assertThat(emissions).isEqualTo(viewStates)
        }
    }

    @Test fun `should not handle any other user interactions`() {

        val result = runCatching {
            viewModel.handle(RequestedFreshContent)
        }
        
        val error = unwrapError(result)

        assertThat(error).isEqualTo(UnsupportedUserInteraction)
    }
}