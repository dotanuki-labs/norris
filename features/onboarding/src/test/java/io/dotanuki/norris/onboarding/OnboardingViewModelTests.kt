package io.dotanuki.norris.onboarding

import app.cash.turbine.test
import io.dotanuki.coroutines.testutils.CoroutinesTestHelper
import io.dotanuki.norris.domain.FetchCategories
import io.dotanuki.norris.domain.model.ChuckNorrisFact
import io.dotanuki.norris.domain.model.RelatedCategory.Available
import io.dotanuki.norris.domain.services.CategoriesCacheService
import io.dotanuki.norris.domain.services.RemoteFactsService
import io.dotanuki.norris.onboarding.OnboardingScreenState.Idle
import io.dotanuki.norris.onboarding.OnboardingScreenState.Launching
import io.dotanuki.norris.onboarding.OnboardingScreenState.Success
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

        viewModel = OnboardingViewModel(usecase)
    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @Test fun `should fetch categories when opening screen`() {
        runBlocking {
            viewModel.run {
                bind().test {
                    val expectedStates = listOf(Idle, Launching, Success)

                    handleApplicationLaunch()

                    val receivedStates = listOf(expectItem(), expectItem(), expectItem())
                    assertThat(receivedStates).isEqualTo(expectedStates)
                }
            }
        }
    }
}
