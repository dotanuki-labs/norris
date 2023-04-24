package io.dotanuki.features.search.presentation

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.dotanuki.features.search.data.SearchesDataSource
import io.dotanuki.features.search.presentation.SearchScreenState.*
import io.dotanuki.platform.android.testing.persistance.PersistanceHelper
import io.dotanuki.platform.jvm.core.networking.errors.HttpDrivenError
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient
import io.dotanuki.platform.jvm.testing.rest.FakeChuckNorrisService
import io.dotanuki.platform.jvm.testing.rest.FakeChuckNorrisService.Scenario
import io.dotanuki.platform.jvm.testing.rest.FakeHttpResilience
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchViewModelTests {

    private val fakeResilience = FakeHttpResilience.create()
    private val fakeService = FakeChuckNorrisService()
    private val chuckNorrisServiceClient = ChuckNorrisServiceClient(fakeService, fakeResilience)
    private val localStorage = PersistanceHelper.storage
    private val dataSource = SearchesDataSource(localStorage, chuckNorrisServiceClient)
    private val viewModel = SearchViewModel(dataSource)

    @Before fun `before each test`() {
        PersistanceHelper.clearStorage()
    }

    @Test fun `at first lunch should display only suggestions`() = runBlocking {

        val categories = listOf("career", "celebrity", "dev")
        fakeService.scenario = Scenario.CategoriesWithSuccess(categories)

        viewModel.bind().test {
            assertThat(awaitItem()).isEqualTo(Idle)

            viewModel.handle(SearchInteraction.OpenedScreen)

            assertThat(awaitItem()).isEqualTo(Loading)
            assertThat(awaitItem()).isEqualTo(Content(categories, history = emptyList()))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test fun `should emit error when loading suggestions`() = runBlocking {

        val serviceDown = HttpDrivenError.RemoteSystem
        fakeService.scenario = Scenario.CategoriesWithError(serviceDown)

        viewModel.bind().test {
            assertThat(awaitItem()).isEqualTo(Idle)

            viewModel.handle(SearchInteraction.OpenedScreen)

            assertThat(awaitItem()).isEqualTo(Loading)
            assertThat(awaitItem()).isEqualTo(Error(serviceDown))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test fun `should proceed saving term chosen from suggestions`() = runBlocking {

        val categories = listOf("career", "celebrity", "dev")
        fakeService.scenario = Scenario.CategoriesWithSuccess(categories)

        PersistanceHelper.registerNewSearch("code")

        viewModel.bind().test {
            assertThat(awaitItem()).isEqualTo(Idle)

            viewModel.handle(SearchInteraction.OpenedScreen)

            assertThat(awaitItem()).isEqualTo(Loading)

            val content = Content(
                suggestions = listOf("career", "celebrity", "dev"),
                history = listOf("code")
            )

            assertThat(awaitItem()).isEqualTo(content)

            viewModel.handle(SearchInteraction.NewQuerySet("math"))

            assertThat(awaitItem()).isEqualTo(Loading)
            assertThat(awaitItem()).isEqualTo(Done)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
