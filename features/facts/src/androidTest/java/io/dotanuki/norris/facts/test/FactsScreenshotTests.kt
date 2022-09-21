package io.dotanuki.norris.facts.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.dotanuki.norris.facts.presentation.FactDisplayRow
import io.dotanuki.norris.facts.presentation.FactsPresentation
import io.dotanuki.norris.facts.presentation.FactsScreenState
import io.dotanuki.norris.facts.ui.FactsView
import io.dotanuki.norris.features.facts.R
import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError
import io.dotanuki.testing.screenshots.ScreenshotDriver
import io.dotanuki.testing.screenshots.ScreenshotTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FactsScreenshotTests {

    private fun driver() = object : ScreenshotDriver<FactsScreeshotsHelperActivity, FactsScreenState> {
        override fun beforeCapturing(target: FactsScreeshotsHelperActivity, state: FactsScreenState) {
            val factsView = target.findViewById<FactsView>(R.id.factsViewRoot)
            listOf(FactsScreenState.Idle, state).forEach { factsView.updateWith(it) }
        }

        override fun imageName(state: FactsScreenState): String =
            "FactsScreenshotTests-${state.javaClass.simpleName}State"
    }

    @get:Rule val screenshotTestRule = ScreenshotTestRule.create(driver())

    @Test fun emptyState() {
        val state = FactsScreenState.Empty
        screenshotTestRule.checkScreenshot(state)
    }

    @Test fun errorState() {
        val state = FactsScreenState.Failed(RemoteServiceIntegrationError.RemoteSystem)
        screenshotTestRule.checkScreenshot(state)
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
        val state = FactsScreenState.Success(presentation)
        screenshotTestRule.checkScreenshot(state)
    }
}
