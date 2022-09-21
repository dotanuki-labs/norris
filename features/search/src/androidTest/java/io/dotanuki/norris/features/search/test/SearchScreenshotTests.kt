package io.dotanuki.norris.features.search.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.dotanuki.norris.features.search.R
import io.dotanuki.norris.features.search.presentation.SearchScreenState
import io.dotanuki.norris.features.search.presentation.SearchScreenState.Error
import io.dotanuki.norris.features.search.ui.SearchView
import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError
import io.dotanuki.testing.screenshots.ScreenshotDriver
import io.dotanuki.testing.screenshots.ScreenshotTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchScreenshotTests {

    private fun driver() = object : ScreenshotDriver<SearchScreenshotsHelperActivity, SearchScreenState> {

        override fun beforeCapturing(target: SearchScreenshotsHelperActivity, state: SearchScreenState) {
            val searchView = target.findViewById<SearchView>(R.id.searchScreenRoot)
            listOf(SearchScreenState.Idle, state).forEach { searchView.updateWith(it) }
        }

        override fun imageName(state: SearchScreenState): String =
            "SearchScreenshotTests-${state.javaClass.simpleName}State"
    }

    @get:Rule val screenshotTestRule = ScreenshotTestRule.create(driver())

    @Test fun errorState() {
        val state = Error(RemoteServiceIntegrationError.RemoteSystem)
        screenshotTestRule.checkScreenshot(state)
    }

    @Test fun contentWithHistory() {
        val state = SearchScreenState.Content(
            suggestions = listOf("science", "celebrity", "humor"),
            history = listOf("code")
        )

        screenshotTestRule.checkScreenshot(state)
    }
}
