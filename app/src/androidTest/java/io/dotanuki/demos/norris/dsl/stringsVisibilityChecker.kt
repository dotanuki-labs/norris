package io.dotanuki.demos.norris.dsl

import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import io.dotanuki.demos.norris.dsl.Visibility.DISPLAYED
import io.dotanuki.demos.norris.dsl.Visibility.HIDDEN

infix fun String.shouldBe(visibility: Visibility) =
    when (visibility) {
        DISPLAYED -> assertDisplayed(this)
        HIDDEN -> assertNotDisplayed(this)
    }

class CheckStringVisibility(private val messageId: Int) {

    infix fun shouldBe(visibility: Visibility) =
        when (visibility) {
            DISPLAYED -> assertDisplayed(messageId)
            HIDDEN -> assertNotDisplayed(messageId)
        }
}
