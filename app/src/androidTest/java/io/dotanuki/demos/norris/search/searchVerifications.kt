package io.dotanuki.demos.norris.search

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import com.schibsted.spain.barista.interaction.BaristaKeyboardInteractions.pressImeActionButton
import com.schibsted.spain.barista.internal.matcher.DisplayedMatchers.displayedWithId
import io.dotanuki.demos.norris.R
import io.dotanuki.demos.norris.dsl.CheckStringVisibility
import io.dotanuki.demos.norris.dsl.ErrorOnEditTextChecker
import io.dotanuki.demos.norris.dsl.ViewVisibilityChecker
import io.dotanuki.demos.norris.util.ChipGroupContentAssertion
import io.dotanuki.norris.navigator.DefineSearchQuery
import org.assertj.core.api.Assertions.assertThat

fun searchQueryChecks(block: SearchQueryChecker.() -> Unit) =
    SearchQueryChecker().apply { block() }

class SearchQueryChecker {
    val loading = ViewVisibilityChecker(R.id.loadingSuggestions)
    val suggestions = ChipsContentChecker(R.id.suggestionChipGroup)
    val pastSearches = ChipsContentChecker(R.id.historyChipGroup)
    val errorOnSuggestions = CheckStringVisibility(R.string.error_snackbar_cannot_load_suggestions)
    val validationError = ErrorOnEditTextChecker(R.id.queryTextInput)

    fun confirmQuery() {
        pressImeActionButton(R.id.queryTextInput)
    }

    fun willForward(scenario: ActivityScenario<*>, query: String) {
        val payload = scenario.result?.resultData
            ?.extras
            ?: throw IllegalArgumentException("Invalid result")

        val payloadKey = DefineSearchQuery.KEY_SEARCH_QUERY

        with(payload) {
            assertThat(containsKey(payloadKey)).isTrue()
            assertThat(getString(payloadKey)).isEqualTo(query)
        }
    }
}

class ChipsContentChecker(private val chipGroupId: Int) {

    infix fun shouldDisplay(chips: List<String>) =
        onView(displayedWithId(chipGroupId)).check(ChipGroupContentAssertion(chips))
}