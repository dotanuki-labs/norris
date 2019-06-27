package io.dotanuki.demos.norris.facts

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.schibsted.spain.barista.assertion.BaristaImageViewAssertions.assertHasDrawable
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.schibsted.spain.barista.internal.matcher.DisplayedMatchers.displayedWithId
import io.dotanuki.demos.norris.R
import io.dotanuki.demos.norris.facts.FactsContent.ABSENT
import io.dotanuki.demos.norris.facts.FactsContent.PRESENT
import io.dotanuki.demos.norris.facts.Visibility.DISPLAYED
import io.dotanuki.demos.norris.facts.Visibility.HIDDEN
import io.dotanuki.demos.norris.util.RecyclerViewContentAssertion
import io.dotanuki.demos.norris.util.SwipeRefreshLayoutMatcher.isRefreshing
import org.hamcrest.Matchers.not

fun assertThat(block: FactsListChecker.() -> Unit) = FactsListChecker().apply { block() }

infix fun String.shouldBe(visibility: Visibility) =
    when (visibility) {
        DISPLAYED -> assertDisplayed(this)
        HIDDEN -> assertNotDisplayed(this)
    }

class FactsListChecker {
    val loadingIndicator = LoadingStateChecker(R.id.factsSwipeToRefresh)
    val errorState = ErrorStateChecker(R.id.errorStateView)
    val errorStateImage = ErrorStateImageChecker(R.id.errorStateImage)
    val errorStateLabel = ErrorStateLabelChecker(R.id.errorStateLabel)
    val content = FactListsSimpleChecker(R.id.factsRecyclerView)
}

class ErrorStateLabelChecker(private val labelId: Int) {

    infix fun shows(message: ErrorMessage) = assertDisplayed(labelId, message.resource)
}

class ErrorStateImageChecker(private val imageId: Int) {

    infix fun shows(image: ErrorImage) = assertHasDrawable(imageId, image.resource)
}

class FactListsSimpleChecker(private val factsRecyclerView: Int) {
    infix fun shouldBe(content: FactsContent) {

        val hasContent = when (content) {
            PRESENT -> true
            ABSENT -> false
        }

        onView(displayedWithId(factsRecyclerView))
            .check(RecyclerViewContentAssertion(hasContent))
    }
}

class LoadingStateChecker(private val widgetId: Int) {
    infix fun shouldBe(visibility: Visibility) =
        when (visibility) {
            DISPLAYED -> checkRefreshing(widgetId)
            HIDDEN -> checkNotRefreshing(widgetId)
        }
}

class ErrorStateChecker(private val view: Int) {
    infix fun shouldBe(visibility: Visibility) =
        when (visibility) {
            DISPLAYED -> assertDisplayed(view)
            HIDDEN -> assertNotDisplayed(view)
        }
}

private fun checkNotRefreshing(refresh: Int) {
    onView(withId(refresh)).check(matches(not(isRefreshing())))
}

private fun checkRefreshing(refresh: Int) {
    onView(withId(refresh)).check(matches(isRefreshing()))
}

enum class Visibility {
    DISPLAYED, HIDDEN
}

enum class FactsContent {
    PRESENT, ABSENT
}

enum class ErrorImage(val resource: Int) {
    IMAGE_BUG_FOUND(R.drawable.img_bug_found)
}

enum class ErrorMessage(val resource: Int) {
    MESSAGE_BUG_FOUND(R.string.error_bug_found)
}
