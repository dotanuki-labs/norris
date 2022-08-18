package io.dotanuki.norris.search.test

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dropbox.differ.SimpleImageComparator
import com.dropbox.dropshots.Dropshots
import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError
import io.dotanuki.norris.search.R
import io.dotanuki.norris.search.presentation.SearchScreenState
import io.dotanuki.norris.search.presentation.SearchScreenState.Error
import io.dotanuki.norris.search.ui.SearchView
import io.dotanuki.testing.screenshots.screenshot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchScreenshotTests {

    @get:Rule val activityScenarioRule = ActivityScenarioRule(SearchScreenshotsHelperActivity::class.java)

    @get:Rule val dropshots = Dropshots(
        imageComparator = SimpleImageComparator(maxDistance = 0.007f)
    )

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
                val searchView = launched.findViewById<SearchView>(R.id.searchScreenRoot)
                listOf(SearchScreenState.Idle, targetState).forEach {
                    searchView.updateWith(it)
                }
            },
            capture = { resumed ->
                dropshots.assertSnapshot(resumed, name = screenshotName)
            }
        )
    }
}
