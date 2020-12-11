package io.dotanuki.demos.norris.search

import androidx.test.espresso.Espresso.onView
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions
import com.schibsted.spain.barista.interaction.BaristaKeyboardInteractions.pressImeActionButton
import com.schibsted.spain.barista.internal.matcher.DisplayedMatchers.displayedWithId
import io.dotanuki.demos.norris.R
import io.dotanuki.demos.norris.dsl.ErrorOnEditTextChecker
import io.dotanuki.demos.norris.dsl.RefreshingStateChecker
import io.dotanuki.demos.norris.util.ChipGroupContentAssertion

fun searchQueryChecks(block: SearchQueryChecker.() -> Unit) =
    SearchQueryChecker().apply { block() }

class SearchQueryChecker {
    val loading = RefreshingStateChecker(R.id.searchSwipeToRefresh)
    val suggestions = ChipsContentChecker(R.id.suggestionChipGroup)
    val pastSearches = ChipsContentChecker(R.id.historyChipGroup)
    val validationError = ErrorOnEditTextChecker(R.id.queryTextInput)

    fun confirmQuery() {
        pressImeActionButton(R.id.queryTextInput)
    }
}

class ChipsContentChecker(private val chipGroupId: Int) {

    infix fun shouldDisplay(chips: List<String>) {
        when {
            chips.isEmpty() -> BaristaVisibilityAssertions.assertNotDisplayed(chipGroupId)
            else -> onView(displayedWithId(chipGroupId)).check(ChipGroupContentAssertion(chips))
        }
    }
}
