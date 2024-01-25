package io.dotanuki.features.facts.util

import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions
import com.adevinta.android.barista.interaction.BaristaMenuClickInteractions
import com.adevinta.android.barista.interaction.BaristaSleepInteractions
import io.dotanuki.features.facts.R

class FactsActivityRobot {
    fun checkDisplayed(targetText: String) {
        BaristaVisibilityAssertions.assertDisplayed(targetText)
    }

    fun clickOnSearchIcon() {
        BaristaMenuClickInteractions.clickMenu(R.id.menu_item_search_facts)
    }

    fun awaitTransition() {
        BaristaSleepInteractions.sleep(500)
    }

    fun checkScreenRedirection() {
        val target = "io.dotanuki.platform.android.testing.app.TestNavigationActivity"
        Intents.intended(hasComponent(target))
    }
}
