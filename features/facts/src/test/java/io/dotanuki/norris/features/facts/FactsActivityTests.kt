package io.dotanuki.norris.features.facts

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.features.facts.di.factsModule
import io.dotanuki.norris.features.facts.presentation.FactDisplayRow
import io.dotanuki.norris.features.facts.presentation.FactsPresentation
import io.dotanuki.norris.features.facts.presentation.FactsScreenState
import io.dotanuki.norris.features.facts.presentation.FactsScreenState.Failed
import io.dotanuki.norris.features.facts.presentation.FactsScreenState.Idle
import io.dotanuki.norris.features.facts.presentation.FactsScreenState.Loading
import io.dotanuki.norris.features.facts.presentation.FactsScreenState.Success
import io.dotanuki.norris.features.facts.ui.FactsActivity
import io.dotanuki.norris.features.facts.ui.FactsView
import io.dotanuki.norris.features.facts.util.FakeFactsEventsHandler
import io.dotanuki.norris.features.facts.util.factsTestModule
import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError
import io.dotanuki.testing.app.TestApplication
import io.dotanuki.testing.app.whenActivityResumed
import io.dotanuki.testing.persistance.PersistanceHelper
import io.dotanuki.testing.rest.RestDataBuilder
import io.dotanuki.testing.rest.RestInfrastructureRule
import io.dotanuki.testing.rest.RestInfrastructureTestModule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(application = TestApplication::class, sdk = [32])
class FactsActivityTests {

    @get:Rule val restInfrastructure = RestInfrastructureRule()

    @Before fun `before each test`() {
        TestApplication.setupWith(
            factsModule,
            factsTestModule,
            RestInfrastructureTestModule(restInfrastructure.server)
        )
    }

    @Test fun `at first lunch, should start on empty state`() {
        whenActivityResumed<FactsActivity> { target ->
            val expectedStates = listOf(Idle, Loading, FactsScreenState.Empty)
            assertThat(target.receivedStates()).isEqualTo(expectedStates)
        }
    }

    @Test fun `when some search done, should display results`() {

        val fact = "Chuck Norris can divide by zero"
        val previousSearch = "humor"

        PersistanceHelper.registerNewSearch("humor")
        restInfrastructure.restScenario(
            status = 200,
            response = RestDataBuilder.factsPayload(previousSearch, fact)
        )

        whenActivityResumed<FactsActivity> { target ->
            val facts = listOf(
                FactDisplayRow(
                    url = RestDataBuilder.FACT_URL,
                    fact = "Chuck Norris can divide by zero",
                    displayWithSmallerFontSize = false
                )
            )

            val presentation = FactsPresentation("humor", facts)

            val expectedStates = listOf(Idle, Loading, Success(presentation))
            assertThat(target.receivedStates()).isEqualTo(expectedStates)
        }
    }

    @Test fun `when remote service fails, should display the error`() {
        restInfrastructure.restScenario(status = 503)
        PersistanceHelper.registerNewSearch("code")

        whenActivityResumed<FactsActivity> { target ->
            val error = Failed(RemoteServiceIntegrationError.RemoteSystem)

            val expectedStates = listOf(Idle, Loading, error)
            assertThat(target.receivedStates()).isEqualTo(expectedStates)
        }
    }

    private fun FactsActivity.receivedStates(): List<FactsScreenState> {
        val rootView = findViewById<FactsView>(R.id.factsViewRoot)
        val callbacks = rootView.eventsHandler as FakeFactsEventsHandler
        return callbacks.trackedStates
    }
}
