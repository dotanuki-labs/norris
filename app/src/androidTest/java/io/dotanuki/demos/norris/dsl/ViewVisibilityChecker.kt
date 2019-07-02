package io.dotanuki.demos.norris.dsl

import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import io.dotanuki.demos.norris.dsl.Visibility.DISPLAYED
import io.dotanuki.demos.norris.dsl.Visibility.HIDDEN

class ViewVisibilityChecker(private val view: Int) {
    infix fun shouldBe(visibility: Visibility) =
        when (visibility) {
            DISPLAYED -> assertDisplayed(view)
            HIDDEN -> assertNotDisplayed(view)
        }
}