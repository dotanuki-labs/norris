package io.dotanuki.demos.norris

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import io.dotanuki.norris.facts.ui.FactsActivity
import io.dotanuki.testing.rest.RestInfrastructureRule
import io.dotanuki.testing.rest.nextSuccess
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NorrisAcceptanceTests {

    @get:Rule val restInfrastructure = RestInfrastructureRule()

    @Before fun beforeEachTest() {
        PersistanceHelper.clearStorage()
    }

    @Test fun shouldPerformFirstSearch_AfterFirstLunch_ByTypingATerm() {

        val searchTerm = "math"
        val suggestions = listOf("career", "dev", "humor")
        val fact = "Chuck Norris can divide by zero"

        val suggestionsPayload = RestDataBuilder.suggestionsPayload(suggestions)
        val factsPayload = RestDataBuilder.factsPayload(searchTerm, fact)

        restInfrastructure.server.run {
            nextSuccess(suggestionsPayload)
            nextSuccess(factsPayload)
        }

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

        val searches = listOf("math", "code").onEach {
            PersistanceHelper.registerNewSearch(it)
        }

        val suggestions = listOf("career", "dev", "humor")
        val mathFact = "Chuck Norris can divide by zero"
        val codeFact = "Null pointer will break with ChuckNorrisException"

        val mathFactPayload = RestDataBuilder.factsPayload("math", mathFact)
        val suggestionsPayload = RestDataBuilder.suggestionsPayload(suggestions)
        val codeFactPayload = RestDataBuilder.factsPayload("code", codeFact)

        restInfrastructure.server.run {
            nextSuccess(mathFactPayload)
            nextSuccess(suggestionsPayload)
            nextSuccess(codeFactPayload)
        }

        startingFrom<FactsActivity> {
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
