package io.dotanuki.features.facts

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.espresso.intent.rule.IntentsRule
import io.dotanuki.features.facts.ui.FactsActivity
import io.dotanuki.features.facts.util.FactsActivityRobot
import io.dotanuki.platform.android.testing.helpers.PrettyEspressoTraces
import io.dotanuki.platform.android.testing.persistence.StorageTestHelper
import leakcanary.LeakAssertions
import org.junit.Rule
import org.junit.Test

class FactsAcceptanceTests {
    init {
        PrettyEspressoTraces.install()
    }

    private val localStorage = StorageTestHelper().storage

    @get:Rule val intentsRule = IntentsRule()

    @Test fun testFactsActivity() {
        val searchTerm = "math"
        val fact = "Chuck Norris can divide by zero"

        launchActivity<FactsActivity>().run {
            FactsActivityRobot().run {
                moveToState(Lifecycle.State.RESUMED)
                awaitTransition()
                checkDisplayed("No facts to show")

                LeakAssertions.assertNoLeaks()
                localStorage.registerNewSearch(searchTerm)

                recreate()
                awaitTransition()
                moveToState(Lifecycle.State.RESUMED)
                checkDisplayed(fact)
                LeakAssertions.assertNoLeaks()

                clickOnSearchIcon()
                checkScreenRedirection()
                close()
                LeakAssertions.assertNoLeaks()
            }
        }
    }
}
