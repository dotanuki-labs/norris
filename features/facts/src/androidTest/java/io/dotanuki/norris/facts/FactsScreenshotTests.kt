package io.dotanuki.norris.facts

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.filters.LargeTest
import com.karumi.shot.ScreenshotTest
import io.dotanuki.norris.facts.presentation.FactDisplayRow
import io.dotanuki.norris.facts.presentation.FactsPresentation
import io.dotanuki.norris.facts.presentation.FactsScreenState
import io.dotanuki.norris.facts.presentation.FactsScreenState.Empty
import io.dotanuki.norris.facts.presentation.FactsScreenState.Failed
import io.dotanuki.norris.facts.presentation.FactsScreenState.Idle
import io.dotanuki.norris.facts.presentation.FactsScreenState.Success
import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError
import org.junit.Test

@LargeTest
class FactsScreenshotTests : ScreenshotTest {

    @Test fun emptyState() {
        checkScreenshot(Empty)
    }

    @Test fun errorState() {
        val error = RemoteServiceIntegrationError.RemoteSystem
        checkScreenshot(Failed(error))
    }

    @Test fun successState() {
        val facts = listOf(
            FactDisplayRow(
                url = "https://chucknorris.io/jokes/12345",
                fact = "Chuck Norris can divide by zero",
                displayWithSmallerFontSize = false
            ),
            FactDisplayRow(
                url = "https://chucknorris.io/jokes/998877",
                fact = "Chuck Norris merges before commit anything on Git",
                displayWithSmallerFontSize = false
            )
        )

        val presentation = FactsPresentation("humor", facts)
        checkScreenshot(Success(presentation))
    }

    private fun checkScreenshot(targetState: FactsScreenState) {
        val states = listOf(Idle, targetState)
        val testActivity = launchActivity<FactsTestActivity>().prepareForScreenshot(states)
        compareScreenshot(testActivity)
    }

    private fun ActivityScenario<FactsTestActivity>.prepareForScreenshot(
        states: List<FactsScreenState>
    ): FactsTestActivity {
        var target: FactsTestActivity? = null
        moveToState(Lifecycle.State.RESUMED)
        onActivity {
            target = it.apply {
                states.forEach { targetState ->
                    screen.updateWith(targetState)
                }
            }
        }
        return if (target != null) {
            target!!
        } else {
            throw IllegalStateException("The activity scenario could not be initialized.")
        }
    }
}

