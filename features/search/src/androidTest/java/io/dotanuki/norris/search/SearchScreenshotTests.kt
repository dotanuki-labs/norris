package io.dotanuki.norris.search

import androidx.test.filters.LargeTest
import com.karumi.shot.ScreenshotTest
import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError
import io.dotanuki.norris.search.presentation.SearchScreenState
import io.dotanuki.norris.search.presentation.SearchScreenState.Error
import io.dotanuki.testing.screenshots.prepareToCaptureScreenshot
import org.junit.Test

@LargeTest
class SearchScreenshotTests : ScreenshotTest {

    @Test fun errorState() {
        val cause = RemoteServiceIntegrationError.RemoteSystem
        checkScreenshot(Error(cause))
    }

    @Test fun contentWithoutHistory() {
        val content = SearchScreenState.Content(
            suggestions = listOf("career", "celebrity", "dev"),
            history = emptyList()
        )

        checkScreenshot(content)
    }

    @Test fun contentWithHistory() {
        val content = SearchScreenState.Content(
            suggestions = listOf("science", "celebrity", "humor"),
            history = listOf("code")
        )

        checkScreenshot(content)
    }

    private fun checkScreenshot(targetState: SearchScreenState) {
        val testActivity = prepareToCaptureScreenshot<SearchTestActivity> { target ->
            listOf(SearchScreenState.Idle, targetState).forEach {
                target.screen.updateWith(it)
            }
        }

        compareScreenshot(testActivity)
    }
}
