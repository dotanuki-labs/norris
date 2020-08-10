package io.dotanuki.norris.onboarding

import app.cash.turbine.test
import io.dotanuki.coroutines.testutils.CoroutinesTestHelper
import io.dotanuki.coroutines.testutils.unwrapError
import io.dotanuki.norris.architecture.StateContainer
import io.dotanuki.norris.architecture.StateMachine
import io.dotanuki.norris.architecture.TaskExecutor
import io.dotanuki.norris.architecture.UnsupportedUserInteraction
import io.dotanuki.norris.architecture.UserInteraction
import io.dotanuki.norris.architecture.UserInteraction.OpenedScreen
import io.dotanuki.norris.architecture.ViewState.FirstLaunch
import io.dotanuki.norris.architecture.ViewState.Loading
import io.dotanuki.norris.architecture.ViewState.Success
import io.dotanuki.norris.domain.FetchCategories
import io.dotanuki.norris.domain.model.ChuckNorrisFact
import io.dotanuki.norris.domain.model.RelatedCategory.Available
import io.dotanuki.norris.domain.services.CategoriesCacheService
import io.dotanuki.norris.domain.services.RemoteFactsService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

class OnboardingViewModelTests {

    @get:Rule val helper = CoroutinesTestHelper()

    private lateinit var viewModel: OnboardingViewModel

    @Before fun `before each test`() {
        val stateMachine = StateMachine<Unit>(
            executor = TaskExecutor.Synchronous(helper.scope),
            container = StateContainer.Unbounded(helper.scope)
        )

        val cache = object : CategoriesCacheService {
            override fun save(categories: List<Available>) = Unit

            override fun cached(): List<Available>? = emptyList()
        }

        val remote = object : RemoteFactsService {
            override suspend fun availableCategories(): List<Available> =
                listOf(
                    Available("politics"),
                    Available("humor")
                )

            override suspend fun fetchFacts(searchTerm: String): List<ChuckNorrisFact> = emptyList()
        }

        val usecase = FetchCategories(cache, remote)

        viewModel = OnboardingViewModel(usecase, stateMachine)
    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @Test fun `should fetch categories when opening screen`() {
        runBlocking {
            viewModel.run {
                bind().test {
                    handle(OpenedScreen)

                    val emissions = listOf(expectItem(), expectItem(), expectItem())
                    val viewStates = listOf(FirstLaunch, Loading.FromEmpty, Success(Unit))

                    assertThat(emissions).isEqualTo(viewStates)
                }
            }
        }
    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @Test fun `should not handle any other user interactions`() {

        val result = runCatching {
            viewModel.handle(UserInteraction.RequestedFreshContent)
        }

        val error = unwrapError(result)

        assertThat(error).isEqualTo(UnsupportedUserInteraction)
    }
}