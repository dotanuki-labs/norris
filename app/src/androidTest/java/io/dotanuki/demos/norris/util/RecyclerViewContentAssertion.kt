package io.dotanuki.demos.norris.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat

class RecyclerViewContentAssertion(private val hasContent: Boolean) : ViewAssertion {

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        val recyclerViewItemCount = adapter?.itemCount ?: 0
        assertThat(hasContent, `is`(recyclerViewItemCount != 0))
    }
}
