package io.dotanuki.features.search

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.espresso.intent.rule.IntentsRule
import io.dotanuki.features.search.ui.SearchActivity
import io.dotanuki.features.search.util.SearchActivityRobot
import io.dotanuki.platform.android.testing.helpers.PrettyEspressoTraces
import io.dotanuki.platform.android.testing.persistence.StorageTestHelper
import leakcanary.LeakAssertions
import org.junit.Rule
import org.junit.Test

class SearchAcceptanceTests {

    init {
        PrettyEspressoTraces.install()
    }

    private val localStorage = StorageTestHelper().storage

    @get:Rule val intentsRule = IntentsRule()

    @Test fun shouldPerformASearch_ByTypingATerm() {

        val searchTerm = "math"
        val suggestions = listOf("career", "dev", "humor")

        launchActivity<SearchActivity>().run {
            SearchActivityRobot().run {
                moveToState(Lifecycle.State.RESUMED)
                awaitTransition()
                checkSuggestions(suggestions)
                performSearch(searchTerm)

                LeakAssertions.assertNoLeaks()
                checkScreenRedirection()
            }
        }
    }

    @Test fun shouldPerformSearch_ByChoosingASuggestion() {

        listOf("code", "math").onEach {
            localStorage.registerNewSearch(it)
        }

        launchActivity<SearchActivity>().run {
            SearchActivityRobot().run {
                moveToState(Lifecycle.State.RESUMED)
                awaitTransition()
                recreate()
                LeakAssertions.assertNoLeaks()

                moveToState(Lifecycle.State.RESUMED)
                awaitTransition()
                clickOnSuggestion("dev")

                checkScreenRedirection()
                close()
                LeakAssertions.assertNoLeaks()
            }
        }
    }
}
