package io.dotanuki.norris.search

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dropbox.dropshots.Dropshots
import com.karumi.shot.ScreenshotTest
import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError
import io.dotanuki.norris.search.presentation.SearchScreenState
import io.dotanuki.norris.search.presentation.SearchScreenState.Error
import io.dotanuki.testing.screenshots.screenshot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchScreenshotTests : ScreenshotTest {

    @get:Rule val activityScenarioRule = ActivityScenarioRule(SearchTestActivity::class.java)
    @get:Rule val dropshots = Dropshots()

    @Test fun errorState() {
        val cause = RemoteServiceIntegrationError.RemoteSystem
        checkScreenshot(Error(cause))
    }

    @Test fun contentWithHistory() {
        val content = SearchScreenState.Content(
            suggestions = listOf("science", "celebrity", "humor"),
            history = listOf("code")
        )

        checkScreenshot(content)
    }

    private fun checkScreenshot(targetState: SearchScreenState) {

        val screenshotName = "SearchScreenshotTests-${targetState.javaClass.simpleName}State"

        activityScenarioRule.scenario.screenshot(
            prepare = { launched ->
                listOf(SearchScreenState.Idle, targetState).forEach {
                    launched.screen.updateWith(it)
                }
            },
            capture = { resumed ->
                dropshots.assertSnapshot(resumed, name = screenshotName)
            }
        )
    }
}
