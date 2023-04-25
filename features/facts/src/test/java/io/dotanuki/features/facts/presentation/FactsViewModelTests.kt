package io.dotanuki.features.facts.presentation

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.dotanuki.features.facts.data.ActualSearchDataSource
import io.dotanuki.features.facts.data.FactsDataSource
import io.dotanuki.features.facts.presentation.FactsScreenState.Empty
import io.dotanuki.features.facts.presentation.FactsScreenState.Failed
import io.dotanuki.features.facts.presentation.FactsScreenState.Idle
import io.dotanuki.features.facts.presentation.FactsScreenState.Loading
import io.dotanuki.features.facts.presentation.FactsScreenState.Success
import io.dotanuki.platform.android.testing.app.TestApplication
import io.dotanuki.platform.android.testing.persistance.PersistanceHelper
import io.dotanuki.platform.jvm.core.networking.errors.NetworkConnectivityError
import io.dotanuki.platform.jvm.testing.rest.RestDataBuilder
import io.dotanuki.platform.jvm.testing.rest.RestScenario
import io.dotanuki.platform.jvm.testing.rest.RestTestHelper
import java.util.UUID
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(application = TestApplication::class, sdk = [32])
class FactsViewModelTests {

    private val restTestHelper = RestTestHelper()
    private val localStorage by lazy { PersistanceHelper.storage }
    private val factsDataSource = FactsDataSource(restTestHelper.createClient())
    private val actualSearchDataSource = ActualSearchDataSource(localStorage)
    private val viewModel = FactsViewModel(factsDataSource, actualSearchDataSource)

    @Test fun `at first lunch, should start on empty state`() = runBlocking {
        viewModel.bind().test {
            assertThat(awaitItem()).isEqualTo(Idle)

            viewModel.handle(FactsUserInteraction.OpenedScreen)

            assertThat(awaitItem()).isEqualTo(FactsScreenState.Loading)
            assertThat(awaitItem()).isEqualTo(Empty)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test fun `given a successful a search, should emit results`() = runBlocking {
        val factId = UUID.randomUUID().toString()
        val divideByZero = "Chuck Norris can divide by zero"
        val previousSearch = "humor"

        PersistanceHelper.registerNewSearch(previousSearch)

        val restScenario = RestScenario.Facts(
            RestDataBuilder.rawSearch(factId, divideByZero)
        )

        restTestHelper.defineScenario(restScenario)

        viewModel.bind().test {
            assertThat(awaitItem()).isEqualTo(Idle)

            viewModel.handle(FactsUserInteraction.OpenedScreen)

            assertThat(awaitItem()).isEqualTo(Loading)

            val facts = listOf(
                FactDisplayRow(
                    url = "https://api.chucknorris.io/jokes/$factId",
                    fact = divideByZero,
                    displayWithSmallerFontSize = false
                )
            )

            val presentation = FactsPresentation(previousSearch, facts)

            assertThat(awaitItem()).isEqualTo(Success(presentation))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test fun `given an unsuccessful a search, should emit error`() = runBlocking {
        val incomingError = NetworkConnectivityError.HostUnreachable

        val restScenario = RestScenario.Error(incomingError)
        restTestHelper.defineScenario(restScenario)

        viewModel.bind().test {
            assertThat(awaitItem()).isEqualTo(Idle)

            viewModel.handle(FactsUserInteraction.OpenedScreen)

            assertThat(awaitItem()).isEqualTo(Loading)
            assertThat(awaitItem()).isEqualTo(Failed(incomingError))
            cancelAndIgnoreRemainingEvents()
        }
    }
}
