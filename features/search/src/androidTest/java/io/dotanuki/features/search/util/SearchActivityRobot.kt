package io.dotanuki.features.search.util

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import com.adevinta.android.barista.interaction.BaristaClickInteractions
import com.adevinta.android.barista.interaction.BaristaEditTextInteractions
import com.adevinta.android.barista.interaction.BaristaKeyboardInteractions
import com.adevinta.android.barista.interaction.BaristaSleepInteractions
import com.adevinta.android.barista.internal.matcher.DisplayedMatchers
import com.google.common.truth.Truth.assertThat
import io.dotanuki.features.search.R
import io.dotanuki.features.search.ui.SearchActivity

class SearchActivityRobot {
    fun performSearch(term: String) {
        BaristaEditTextInteractions.writeTo(R.id.queryTextInput, term)
        BaristaKeyboardInteractions.pressImeActionButton(R.id.queryTextInput)
    }

    fun checkSuggestions(suggestions: List<String>) {
        Espresso.onView(DisplayedMatchers.displayedWithId(R.id.suggestionChipGroup)).check(
            ChipGroupContentAssertion(suggestions)
        )
    }

    fun clickOnSuggestion(chip: String) {
        BaristaClickInteractions.clickOn(chip)
    }

    fun ActivityScenario<SearchActivity>.checkScreenRedirection() {
        assertThat(state).isEqualTo(Lifecycle.State.DESTROYED)
    }

    fun awaitTransition() {
        BaristaSleepInteractions.sleep(500)
    }
}
