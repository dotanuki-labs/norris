package io.dotanuki.demos.norris

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.rule.flaky.FlakyTestRule
import com.schibsted.spain.barista.rule.flaky.Repeat
import io.dotanuki.norris.facts.ui.FactsActivity
import io.dotanuki.testing.persistance.PersistanceHelper
import io.dotanuki.testing.rest.RestDataBuilder
import io.dotanuki.testing.rest.RestInfrastructureRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NorrisAcceptanceTests {

    private val restInfrastructure = RestInfrastructureRule(customPort = 4242)
    private val stressedExecution = FlakyTestRule()

    @get:Rule val rules: RuleChain =
        RuleChain.outerRule(stressedExecution).around(restInfrastructure)

    @Before fun beforeEachTest() {
        PersistanceHelper.clearStorage()
    }

    @Repeat @Test fun shouldPerformFirstSearch_AfterFirstLunch_ByTypingATerm() {

        val searchTerm = "math"
        val suggestions = listOf("career", "dev", "humor")
        val fact = "Chuck Norris can divide by zero"

        val suggestionsPayload = RestDataBuilder.suggestionsPayload(suggestions)
        val factsPayload = RestDataBuilder.factsPayload(searchTerm, fact)

        restInfrastructure.run {
            restScenario(200, suggestionsPayload)
            restScenario(200, factsPayload)
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

    @Repeat @Test fun shouldPerformASecondSearch_ByChosingASuggestion() {

        val searches = listOf("math", "code").onEach {
            PersistanceHelper.registerNewSearch(it)
        }

        val suggestions = listOf("career", "dev", "humor")
        val mathFact = "Chuck Norris can divide by zero"
        val codeFact = "Null pointer will break with ChuckNorrisException"

        val mathFactPayload = RestDataBuilder.factsPayload("math", mathFact)
        val suggestionsPayload = RestDataBuilder.suggestionsPayload(suggestions)
        val codeFactPayload = RestDataBuilder.factsPayload("code", codeFact)

        restInfrastructure.run {
            restScenario(200, mathFactPayload)
            restScenario(200, suggestionsPayload)
            restScenario(200, codeFactPayload)
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
