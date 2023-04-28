package io.dotanuki.features.facts

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.dotanuki.features.facts.presentation.FactDisplayRow
import io.dotanuki.features.facts.presentation.FactsPresentation
import io.dotanuki.features.facts.presentation.FactsScreenState
import io.dotanuki.features.facts.util.FactsScreenshotsDriver
import io.dotanuki.platform.android.testing.screenshots.ScreenshotTestRule
import io.dotanuki.platform.jvm.core.rest.HttpNetworkingError
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FactsScreenshotTests {

    @get:Rule val screenshotTestRule = ScreenshotTestRule.create(FactsScreenshotsDriver)

    @Test fun emptyState() {
        val state = FactsScreenState.Empty
        screenshotTestRule.checkScreenshot(state)
    }

    @Test fun errorState() {
        val state = FactsScreenState.Failed(HttpNetworkingError.Restful.Server(500))
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
