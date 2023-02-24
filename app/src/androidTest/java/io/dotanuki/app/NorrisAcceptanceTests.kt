package io.dotanuki.app

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import io.dotanuki.features.facts.ui.FactsActivity
import io.dotanuki.platform.android.testing.persistance.PersistanceHelper
import leakcanary.DetectLeaksAfterTestSuccess
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NorrisAcceptanceTests {

    @get:Rule val noMemoryLeaks = DetectLeaksAfterTestSuccess()

    init {
        PrettyEspressoErrors.install()
    }

    @Before fun beforeEachTest() {
        PersistanceHelper.clearStorage()
    }

    @Test fun shouldPerformFirstSearch_AfterFirstLunch_ByTypingATerm() {

        val searchTerm = "math"
        val suggestions = listOf("career", "dev", "humor")
        val fact = "Chuck Norris can divide by zero"

        startingFrom<FactsActivity> {
            checkEmptyState()
            clickOnSearchIcon()
            awaitTransition()

            checkSuggestions(suggestions)
            performSearch(searchTerm)
            awaitTransition()

            checkDisplayed(fact)
        }
    }

    @Test fun shouldPerformASecondSearch_ByChosingASuggestion() {

        listOf("code", "math").onEach {
            PersistanceHelper.registerNewSearch(it)
        }

        val mathFact = "Chuck Norris can divide by zero"
        val codeFact = "Null pointer will break with ChuckNorrisException"

        startingFrom<FactsActivity> {
            awaitTransition()

            checkDisplayed(mathFact)
            clickOnSearchIcon()

            awaitTransition()
            clickOn("dev")

            awaitTransition()
            checkDisplayed(codeFact)
        }
    }
}
