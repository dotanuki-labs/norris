package io.dotanuki.features.search

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.dotanuki.features.search.presentation.SearchScreenState
import io.dotanuki.features.search.presentation.SearchScreenState.Failed
import io.dotanuki.features.search.util.SearchScreenshotsDriver
import io.dotanuki.platform.android.testing.screenshots.ScreenshotMatcher
import io.dotanuki.platform.jvm.core.rest.HttpNetworkingError
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchScreenshotTests {

    @get:Rule val screenshotMatcher = ScreenshotMatcher.create(SearchScreenshotsDriver)

    @Test fun errorState() {
        val error = HttpNetworkingError.Restful.Server(500)
        val state = Failed(error)
        screenshotMatcher.match(state)
    }

    @Test fun contentWithHistory() {
        val state = SearchScreenState.Success(
            suggestions = listOf("science", "celebrity", "humor"),
            history = listOf("code")
        )

        screenshotMatcher.match(state)
    }
}
