package io.dotanuki.norris.facts

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dropbox.differ.SimpleImageComparator
import com.dropbox.dropshots.Dropshots
import io.dotanuki.norris.facts.presentation.FactDisplayRow
import io.dotanuki.norris.facts.presentation.FactsPresentation
import io.dotanuki.norris.facts.presentation.FactsScreenState
import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError
import io.dotanuki.testing.screenshots.screenshot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FactsScreenshotTests {

    @get:Rule val activityScenarioRule = ActivityScenarioRule(FactsTestActivity::class.java)
    @get:Rule val dropshots = Dropshots(
        imageComparator = SimpleImageComparator(maxDistance = 0.007f)
    )

    @Test fun emptyState() {
        checkScreenshot(FactsScreenState.Empty)
    }

    @Test fun errorState() {
        val error = RemoteServiceIntegrationError.RemoteSystem
        checkScreenshot(FactsScreenState.Failed(error))
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
        checkScreenshot(FactsScreenState.Success(presentation))
    }

    private fun checkScreenshot(targetState: FactsScreenState) {

        val screenshotName = "FactsScreenshotTests-${targetState.javaClass.simpleName}State"

        activityScenarioRule.scenario.screenshot(
            prepare = { launched ->
                listOf(FactsScreenState.Idle, targetState).forEach {
                    launched.screen.updateWith(it)
                }
            },
            capture = { resumed ->
                dropshots.assertSnapshot(resumed, name = screenshotName)
            }
        )
    }
}
