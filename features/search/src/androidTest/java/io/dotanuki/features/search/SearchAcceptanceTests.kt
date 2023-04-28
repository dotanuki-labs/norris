package io.dotanuki.features.search

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.espresso.intent.rule.IntentsRule
import io.dotanuki.features.search.ui.SearchActivity
import io.dotanuki.features.search.util.SearchActivityRobot
import io.dotanuki.platform.android.testing.helpers.ViewHierarchyBeautifier
import io.dotanuki.platform.android.testing.persistance.StorageTestHelper
import leakcanary.LeakAssertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchAcceptanceTests {

    init {
        ViewHierarchyBeautifier.install()
    }

    private val storageTestHelper = StorageTestHelper()

    @get:Rule val intentsRule = IntentsRule()

    @Before fun beforeEachTest() {
        storageTestHelper.clearStorage()
    }

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

    @Test fun shouldPerformSearch_ByChosingASuggestion() {

        listOf("code", "math").onEach {
            storageTestHelper.registerNewSearch(it)
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
                LeakAssertions.assertNoLeaks()
            }
        }
    }
}
