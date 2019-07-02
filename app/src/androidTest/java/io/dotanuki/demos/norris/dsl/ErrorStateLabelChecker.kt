package io.dotanuki.demos.norris.dsl

import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed

class ErrorStateLabelChecker(private val labelId: Int) {

    infix fun shows(message: ErrorMessage) = assertDisplayed(labelId, message.resource)
}
