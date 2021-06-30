package io.dotanuki.demos.norris

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaEditTextInteractions.writeTo
import com.schibsted.spain.barista.interaction.BaristaKeyboardInteractions.pressImeActionButton
import com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions
import com.schibsted.spain.barista.internal.matcher.DisplayedMatchers.displayedWithId
import io.dotanuki.norris.facts.R as FactsR
import io.dotanuki.norris.search.R as SearchR
import io.dotanuki.norris.sharedassets.R as SharedR

inline fun <reified T : Activity> startingFrom(block: ScreenRobot.() -> Unit) {
    launchActivity<T>().run {
        moveToState(Lifecycle.State.RESUMED)
        ScreenRobot().apply(block)
        close()
    }
}

class ScreenRobot {

    fun checkDisplayed(targetText: String) {
        assertDisplayed(targetText)
    }

    fun clickOnSearchIcon() {
        clickMenu(FactsR.id.menu_item_search_facts)
    }

    fun performSearch(term: String) {
        writeTo(SearchR.id.queryTextInput, term)
        pressImeActionButton(SearchR.id.queryTextInput)
    }

    fun checkSuggestions(suggestions: List<String>) {
        onView(displayedWithId(SearchR.id.suggestionChipGroup)).check(
            ChipGroupContentAssertion(suggestions)
        )
    }

    fun checkHistory(previous: List<String>) {
        onView(displayedWithId(SearchR.id.historyChipGroup))
            .check(
                ChipGroupContentAssertion(previous)
            )
    }

    fun checkEmptyState() {
        assertDisplayed(SharedR.string.error_no_results)
    }

    fun awaitTransition() {
        BaristaSleepInteractions.sleep(500)
    }
}
