package io.dotanuki.features.search.presentation

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.dotanuki.features.search.data.SearchesDataSource
import io.dotanuki.features.search.presentation.SearchScreenState.Done
import io.dotanuki.features.search.presentation.SearchScreenState.Failed
import io.dotanuki.features.search.presentation.SearchScreenState.Idle
import io.dotanuki.features.search.presentation.SearchScreenState.Loading
import io.dotanuki.features.search.presentation.SearchScreenState.Success
import io.dotanuki.platform.android.testing.persistance.PersistanceHelper
import io.dotanuki.platform.jvm.core.networking.errors.HttpDrivenError
import io.dotanuki.platform.jvm.testing.rest.RestScenario
import io.dotanuki.platform.jvm.testing.rest.RestTestHelper
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchViewModelTests {

    private val restTestHelper = RestTestHelper()
    private val localStorage = PersistanceHelper.storage
    private val dataSource = SearchesDataSource(localStorage, restTestHelper.createClient())
    private val viewModel = SearchViewModel(dataSource)

    private val categories = listOf("career", "celebrity", "dev")

    @Before fun `before each test`() {
        PersistanceHelper.clearStorage()
    }

    @Test fun `at first lunch should display only suggestions`() = runBlocking {
        val scenario = RestScenario.Categories(categories)
        restTestHelper.defineScenario(scenario)

        viewModel.bind().test {
            assertThat(awaitItem()).isEqualTo(Idle)

            viewModel.handle(SearchInteraction.OpenedScreen)

            assertThat(awaitItem()).isEqualTo(Loading)
            assertThat(awaitItem()).isEqualTo(Success(categories, history = emptyList()))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test fun `should emit error when loading suggestions`() = runBlocking {
        val serviceDown = HttpDrivenError.RemoteSystem
        val scenario = RestScenario.Error(serviceDown)
        restTestHelper.defineScenario(scenario)

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
        restTestHelper.defineScenario(scenario)

        PersistanceHelper.registerNewSearch("code")

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
