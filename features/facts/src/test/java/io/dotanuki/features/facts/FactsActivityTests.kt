package io.dotanuki.features.facts

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.dotanuki.features.facts.presentation.FactDisplayRow
import io.dotanuki.features.facts.presentation.FactsPresentation
import io.dotanuki.features.facts.presentation.FactsScreenState
import io.dotanuki.features.facts.presentation.FactsScreenState.Idle
import io.dotanuki.features.facts.presentation.FactsScreenState.Loading
import io.dotanuki.features.facts.presentation.FactsScreenState.Success
import io.dotanuki.features.facts.ui.FactsActivity
import io.dotanuki.features.facts.ui.FactsView
import io.dotanuki.platform.android.testing.app.TestApplication
import io.dotanuki.platform.android.testing.app.whenActivityResumed
import io.dotanuki.platform.android.testing.persistance.PersistanceHelper
import io.dotanuki.platform.jvm.testing.rest.RestDataBuilder
import io.dotanuki.platform.jvm.testing.rest.RestInfrastructureRule
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
//        TestApplication.setupWith(
//            factsModule,
//            factsTestModule,
//            RestInfrastructureTestModule(restInfrastructure.server)
//        )
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

    private fun FactsActivity.receivedStates(): List<FactsScreenState> =
        findViewById<FactsView>(R.id.factsViewRoot).receivedStates
}
