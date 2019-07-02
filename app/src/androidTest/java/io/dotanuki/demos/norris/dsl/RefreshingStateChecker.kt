package io.dotanuki.demos.norris.dsl

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import io.dotanuki.demos.norris.util.SwipeRefreshLayoutMatcher
import org.hamcrest.Matchers.not

class RefreshingStateChecker(private val widgetId: Int) {

    infix fun shouldBe(visibility: Visibility) =
        when (visibility) {
            Visibility.DISPLAYED -> checkRefreshing(widgetId)
            Visibility.HIDDEN -> checkNotRefreshing(widgetId)
        }

    private fun checkNotRefreshing(refresh: Int) {
        onView(withId(refresh))
            .check(matches(not(SwipeRefreshLayoutMatcher.isRefreshing())))
    }

    private fun checkRefreshing(refresh: Int) {
        onView(withId(refresh))
            .check(matches(SwipeRefreshLayoutMatcher.isRefreshing()))
    }
}