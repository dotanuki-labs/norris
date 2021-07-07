package io.dotanuki.norri.facts

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.dotanuki.norri.facts.util.FakeFactsScreen
import io.dotanuki.norri.facts.util.FakeFactsScreen.Companion.factsScreen
import io.dotanuki.norri.facts.util.factsTestModule
import io.dotanuki.norris.facts.di.factsModule
import io.dotanuki.norris.facts.presentation.FactDisplayRow
import io.dotanuki.norris.facts.presentation.FactsPresentation
import io.dotanuki.norris.facts.presentation.FactsScreenState
import io.dotanuki.norris.facts.presentation.FactsScreenState.Failed
import io.dotanuki.norris.facts.presentation.FactsScreenState.Idle
import io.dotanuki.norris.facts.presentation.FactsScreenState.Loading
import io.dotanuki.norris.facts.presentation.FactsScreenState.Success
import io.dotanuki.norris.facts.ui.FactsActivity
import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError
import io.dotanuki.testing.app.TestApplication
import io.dotanuki.testing.app.whenActivityResumed
import io.dotanuki.testing.rest.RestDataBuilder
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FactsActivityTests {

    private lateinit var testApp: TestApplication
    private lateinit var screen: FakeFactsScreen

    @Before fun `before each test`() {
        testApp = TestApplication.setupWith(factsModule, factsTestModule)
        screen = testApp.factsScreen()
    }

    @Test fun `at first lunch, should start on empty state`() {
        whenActivityResumed<FactsActivity> {
            assertThat(screen.isLinked).isTrue()

            val expectedStates = listOf(Idle, Loading, FactsScreenState.Empty)
            assertThat(screen.trackedStates).isEqualTo(expectedStates)
        }
    }

    @Test fun `when some search done, should display results`() {

        val fact = "Chuck Norris can divide by zero"
        val previousSearch = "humor"
        val payload = RestDataBuilder.factsPayload(previousSearch, fact)

        with(testApp) {
            api.fakeSearch = payload
            localStorage.registerNewSearch("humor")
        }

        whenActivityResumed<FactsActivity> {
            val facts = listOf(
                FactDisplayRow(
                    url = RestDataBuilder.FACT_URL,
                    fact = "Chuck Norris can divide by zero",
                    displayWithSmallerFontSize = false
                )
            )

            val presentation = FactsPresentation("humor", facts)
            val expectedStates = listOf(Idle, Loading, Success(presentation))
            assertThat(screen.trackedStates).isEqualTo(expectedStates)
        }
    }

    @Test fun `when remote service fails, should display the error`() {
        with(testApp) {
            api.errorMode = true
            localStorage.registerNewSearch("code")
        }

        whenActivityResumed<FactsActivity> {
            val error = Failed(RemoteServiceIntegrationError.RemoteSystem)
            val expectedStates = listOf(Idle, Loading, error)
            assertThat(screen.trackedStates).isEqualTo(expectedStates)
        }
    }
}
