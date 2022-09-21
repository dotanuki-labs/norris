package io.dotanuki.demos.norris.test

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import io.dotanuki.norris.features.facts.ui.FactsActivity
import io.dotanuki.testing.persistance.PersistanceHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NorrisAcceptanceTests {

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

        val searches = listOf("code", "math").onEach {
            PersistanceHelper.registerNewSearch(it)
        }

        val mathFact = "Chuck Norris can divide by zero"
        val codeFact = "Null pointer will break with ChuckNorrisException"

        startingFrom<FactsActivity> {
            awaitTransition()

            checkDisplayed(mathFact)
            clickOnSearchIcon()

            awaitTransition()
            checkHistory(searches)
            clickOn("code")

            awaitTransition()
            checkDisplayed(codeFact)
        }
    }
}
