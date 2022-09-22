package io.dotanuki.demos.norris.test

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaEditTextInteractions.writeTo
import com.adevinta.android.barista.interaction.BaristaKeyboardInteractions.pressImeActionButton
import com.adevinta.android.barista.interaction.BaristaMenuClickInteractions.clickMenu
import com.adevinta.android.barista.interaction.BaristaSleepInteractions
import com.adevinta.android.barista.internal.matcher.DisplayedMatchers.displayedWithId
import io.dotanuki.features.facts.R as FactsR
import io.dotanuki.features.search.R as SearchR

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
        assertDisplayed("No facts to show")
    }

    fun awaitTransition() {
        BaristaSleepInteractions.sleep(500)
    }
}
