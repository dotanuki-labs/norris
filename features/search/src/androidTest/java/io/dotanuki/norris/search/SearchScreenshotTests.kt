package io.dotanuki.norris.search

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.filters.LargeTest
import com.karumi.shot.ScreenshotTest
import io.dotanuki.norris.networking.errors.RemoteServiceIntegrationError
import io.dotanuki.norris.search.presentation.SearchScreenState
import io.dotanuki.norris.search.presentation.SearchScreenState.Error
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
        val states = listOf(SearchScreenState.Idle, targetState)
        val testActivity = launchActivity<SearchTestActivity>().prepareForScreenshot(states)
        compareScreenshot(testActivity)
    }

    private fun ActivityScenario<SearchTestActivity>.prepareForScreenshot(
        states: List<SearchScreenState>
    ): SearchTestActivity {
        var target: SearchTestActivity? = null
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
