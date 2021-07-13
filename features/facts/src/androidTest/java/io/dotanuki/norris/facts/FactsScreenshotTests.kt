package io.dotanuki.norris.facts

import androidx.test.filters.LargeTest
import com.karumi.shot.ScreenshotTest
import io.dotanuki.norris.facts.presentation.FactDisplayRow
import io.dotanuki.norris.facts.presentation.FactsPresentation
import io.dotanuki.norris.facts.presentation.FactsScreenState
import io.dotanuki.norris.facts.presentation.FactsScreenState.Empty
import io.dotanuki.norris.facts.presentation.FactsScreenState.Failed
import io.dotanuki.norris.facts.presentation.FactsScreenState.Success
import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError
import io.dotanuki.testing.screenshots.prepareToCaptureScreenshot
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
        val testActivity = prepareToCaptureScreenshot<FactsTestActivity> { target ->
            listOf(FactsScreenState.Idle, targetState).forEach {
                target.screen.updateWith(it)
            }
        }
        compareScreenshot(testActivity)
    }
}
