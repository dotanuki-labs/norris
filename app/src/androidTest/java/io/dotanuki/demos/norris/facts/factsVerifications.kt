package io.dotanuki.demos.norris.facts

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.internal.matcher.DisplayedMatchers.displayedWithId
import io.dotanuki.demos.norris.R
import io.dotanuki.demos.norris.facts.FactsContent.absent
import io.dotanuki.demos.norris.facts.FactsContent.present
import io.dotanuki.demos.norris.facts.Visibility.displayed
import io.dotanuki.demos.norris.facts.Visibility.hidden
import io.dotanuki.demos.norris.util.RecyclerViewContentAssertion
import io.dotanuki.demos.norris.util.SwipeRefreshLayoutMatchers.isRefreshing
import org.hamcrest.Matchers.not

fun assertThat(block: FactsListChecker.() -> Unit) = FactsListChecker().apply { block() }

infix fun String.shouldBe(visibility: Visibility) =
    when (visibility) {
        is displayed -> assertDisplayed(this)
        is hidden -> assertNotDisplayed(this)
    }

class FactsListChecker {

    val loadingIndicator = LoadingStateChecker(R.id.factsSwipeToRefresh)

    val errorState = ErrorStateChecker(R.id.errorStateView)

    val content = FactListsSimpleChecker(R.id.factsRecyclerView)
}

class FactListsSimpleChecker(private val factsRecyclerView: Int) {
    infix fun shouldBe(content: FactsContent) {

        val hasContent = when (content) {
            is present -> true
            is absent -> false
        }

        onView(displayedWithId(factsRecyclerView))
            .check(RecyclerViewContentAssertion(hasContent))
    }
}

class LoadingStateChecker(private val widgetId: Int) {
    infix fun shouldBe(visibility: Visibility) =
        when (visibility) {
            is displayed -> checkRefreshing(widgetId)
            is hidden -> checkNotRefreshing(widgetId)
        }
}

class ErrorStateChecker(private val view: Int) {
    infix fun shouldBe(visibility: Visibility) =
        when (visibility) {
            is displayed -> assertDisplayed(view)
            is hidden -> assertNotDisplayed(view)
        }
}

private fun checkNotRefreshing(refresh: Int) {
    onView(withId(refresh)).check(matches(not(isRefreshing())))
}

private fun checkRefreshing(refresh: Int) {
    onView(withId(refresh)).check(matches(isRefreshing()))
}

sealed class Visibility {
    object displayed : Visibility()
    object hidden : Visibility()
}

sealed class FactsContent {
    object present : FactsContent()
    object absent : FactsContent()
}
