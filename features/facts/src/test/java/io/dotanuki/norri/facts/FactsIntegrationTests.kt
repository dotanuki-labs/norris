package io.dotanuki.norri.facts

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.dotanuki.norris.facts.di.factsModule
import io.dotanuki.norris.facts.presentation.FactDisplayRow
import io.dotanuki.norris.facts.presentation.FactsPresentation
import io.dotanuki.norris.facts.presentation.FactsScreenState.Empty
import io.dotanuki.norris.facts.presentation.FactsScreenState.Failed
import io.dotanuki.norris.facts.presentation.FactsScreenState.Idle
import io.dotanuki.norris.facts.presentation.FactsScreenState.Loading
import io.dotanuki.norris.facts.presentation.FactsScreenState.Success
import io.dotanuki.norris.facts.ui.FactsActivity
import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError
import io.dotanuki.testing.app.TestApplication
import io.dotanuki.testing.app.activityScenario
import io.dotanuki.testing.app.awaitPendingExecutions
import io.dotanuki.testing.rest.RestDataBuilder
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FactsIntegrationTests {

    private lateinit var testApplication: TestApplication

    @Before fun `before each test`() {
        testApplication = TestApplication.setupWith(factsModule)
    }

    @Test fun `at first lunch, should start on empty state`() {
        activityScenario<FactsActivity> {
            whenResumed { target ->
                awaitPendingExecutions()

                val expectedStates = listOf(Idle, Loading, Empty)
                assertThat(target.states).isEqualTo(expectedStates)
            }
        }
    }

    @Test fun `when some search done, should display results`() {

        val fact = "Chuck Norris can divide by zero"
        val previousSearch = "humor"
        val payload = RestDataBuilder.factsPayload(previousSearch, fact)

        with(testApplication) {
            api.fakeSearch = payload
            localStorage.registerNewSearch("humor")
        }

        activityScenario<FactsActivity> {
            whenResumed { target ->

                awaitPendingExecutions()

                val facts = listOf(
                    FactDisplayRow(
                        url = RestDataBuilder.FACT_URL,
                        fact = "Chuck Norris can divide by zero",
                        displayWithSmallerFontSize = false
                    )
                )
                val presentation = FactsPresentation("humor", facts)
                val expectedStates = listOf(Idle, Loading, Success(presentation))
                assertThat(target.states).isEqualTo(expectedStates)
            }
        }
    }

    @Test fun `when remote service fails, should display the error`() {
        with(testApplication) {
            api.errorMode = true
            localStorage.registerNewSearch("code")
        }

        activityScenario<FactsActivity> {
            whenResumed { target ->

                awaitPendingExecutions()

                val error = Failed(RemoteServiceIntegrationError.RemoteSystem)
                val expectedStates = listOf(Idle, Loading, error)
                assertThat(target.states).isEqualTo(expectedStates)
            }
        }
    }
}
