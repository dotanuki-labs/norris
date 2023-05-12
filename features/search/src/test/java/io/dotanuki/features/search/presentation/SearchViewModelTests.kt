package io.dotanuki.features.search.presentation

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.dotanuki.features.search.di.SearchContext
import io.dotanuki.features.search.di.SearchViewModelFactory
import io.dotanuki.features.search.presentation.SearchScreenState.Done
import io.dotanuki.features.search.presentation.SearchScreenState.Failed
import io.dotanuki.features.search.presentation.SearchScreenState.Idle
import io.dotanuki.features.search.presentation.SearchScreenState.Loading
import io.dotanuki.features.search.presentation.SearchScreenState.Success
import io.dotanuki.platform.android.testing.persistance.StorageTestHelper
import io.dotanuki.platform.jvm.core.rest.HttpNetworkingError
import io.dotanuki.platform.jvm.testing.rest.RestScenario
import io.dotanuki.platform.jvm.testing.rest.RestTestHelper
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchViewModelTests {

    private val restHelper = RestTestHelper()
    private val storageHelper = StorageTestHelper()

    private val testContext = SearchContext(restHelper.restClient, storageHelper.storage)
    private val viewModel = with(testContext) { SearchViewModelFactory().create() }

    private val categories = listOf("career", "celebrity", "dev")

    @Test fun `at first lunch should display only suggestions`() = runBlocking {
        val scenario = RestScenario.Categories(categories)
        restHelper.defineScenario(scenario)

        viewModel.bind().test {
            assertThat(awaitItem()).isEqualTo(Idle)

            viewModel.handle(SearchInteraction.OpenedScreen)

            assertThat(awaitItem()).isEqualTo(Loading)
            assertThat(awaitItem()).isEqualTo(Success(categories, history = emptyList()))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test fun `should emit error when loading suggestions`() = runBlocking {
        val serviceDown = HttpNetworkingError.Restful.Server(500)
        val scenario = RestScenario.Error(serviceDown)
        restHelper.defineScenario(scenario)

        viewModel.bind().test {
            assertThat(awaitItem()).isEqualTo(Idle)

            viewModel.handle(SearchInteraction.OpenedScreen)

            assertThat(awaitItem()).isEqualTo(Loading)
            assertThat(awaitItem()).isEqualTo(Failed(serviceDown))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test fun `should proceed saving term chosen from suggestions`() = runBlocking {
        val scenario = RestScenario.Categories(categories)
        restHelper.defineScenario(scenario)

        storageHelper.storage.registerNewSearch("code")

        viewModel.bind().test {
            assertThat(awaitItem()).isEqualTo(Idle)

            viewModel.handle(SearchInteraction.OpenedScreen)

            assertThat(awaitItem()).isEqualTo(Loading)

            val success = Success(
                suggestions = listOf("career", "celebrity", "dev"),
                history = listOf("code")
            )

            assertThat(awaitItem()).isEqualTo(success)

            viewModel.handle(SearchInteraction.NewQuerySet("math"))

            assertThat(awaitItem()).isEqualTo(Loading)
            assertThat(awaitItem()).isEqualTo(Done)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
