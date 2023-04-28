package io.dotanuki.features.facts

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.espresso.intent.rule.IntentsRule
import io.dotanuki.features.facts.ui.FactsActivity
import io.dotanuki.features.facts.util.FactsActivityRobot
import io.dotanuki.platform.android.testing.helpers.ViewHierarchyBeautifier
import io.dotanuki.platform.android.testing.persistance.StorageTestHelper
import leakcanary.LeakAssertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FactsAcceptanceTests {

    init {
        ViewHierarchyBeautifier.install()
    }

    private val storageTestHelper = StorageTestHelper()

    @get:Rule val intentsRule = IntentsRule()

    @Before fun beforeEachTest() {
        storageTestHelper.clearStorage()
    }

    @Test fun testFactsActivity() {
        val searchTerm = "math"
        val fact = "Chuck Norris can divide by zero"

        launchActivity<FactsActivity>().run {
            FactsActivityRobot().run {
                moveToState(Lifecycle.State.RESUMED)
                awaitTransition()
                checkDisplayed("No facts to show")

                LeakAssertions.assertNoLeaks()
                storageTestHelper.registerNewSearch(searchTerm)

                recreate()
                awaitTransition()
                moveToState(Lifecycle.State.RESUMED)
                checkDisplayed(fact)
                LeakAssertions.assertNoLeaks()

                clickOnSearchIcon()
                checkScreenRedirection()
                close()
            }
        }
    }
}
