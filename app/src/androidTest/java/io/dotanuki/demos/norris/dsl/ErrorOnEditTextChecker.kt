package io.dotanuki.demos.norris.dsl

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import io.dotanuki.demos.norris.util.ErrorOnEditTextMatcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not

class ErrorOnEditTextChecker(private val textFieldId: Int) {
    infix fun shouldBe(visibility: Visibility) =
        when (visibility) {
            Visibility.DISPLAYED -> checkDisplayed()
            Visibility.HIDDEN -> checkNotDisplayed()
        }

    private fun checkDisplayed() {
        onView(allOf(withId(textFieldId), isDisplayed()))
            .check(matches(ErrorOnEditTextMatcher()))
    }

    private fun checkNotDisplayed() {
        onView(allOf(withId(textFieldId), not(isDisplayed())))
            .check(matches(ErrorOnEditTextMatcher()))
    }
}