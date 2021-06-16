package io.dotanuki.norris.onboarding

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.domain.FetchCategories
import io.dotanuki.norris.domain.model.ChuckNorrisFact
import io.dotanuki.norris.domain.model.RelatedCategory.Available
import io.dotanuki.norris.domain.services.CategoriesCacheService
import io.dotanuki.norris.domain.services.RemoteFactsService
import io.dotanuki.testing.coroutines.CoroutinesTestHelper
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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

    @Test fun `should fetch categories when opening screen`() {
        runBlocking {
            viewModel.run {
                bind().test {
                    val expectedStates = listOf(
                        OnboardingScreenState.Idle,
                        OnboardingScreenState.Launching,
                        OnboardingScreenState.Success
                    )

                    handleApplicationLaunch()

                    val receivedStates = listOf(expectItem(), expectItem(), expectItem())
                    assertThat(receivedStates).isEqualTo(expectedStates)
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }
}
