package io.dotanuki.demos.norris.util

import android.view.View
import android.widget.EditText
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

class ErrorOnEditTextMatcher : BoundedMatcher<View, EditText>(EditText::class.java) {

    override fun describeTo(description: Description) {
        description.appendText("has no error text: ")
    }

    override fun matchesSafely(item: EditText?): Boolean {
        return item?.error.let { true }
    }
}