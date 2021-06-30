package io.dotanuki.demos.norris

import android.view.View
import androidx.core.view.children
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.common.truth.Truth.assertThat

class ChipGroupContentAssertion(private val chips: List<String>) : ViewAssertion {

    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        noViewFoundException
            ?.let { throw it }
            ?: checkContent(view)
    }

    private fun checkContent(view: View?) {
        view?.let {
            if (view is ChipGroup) {
                view.children.forEach {
                    val chip = it as Chip
                    assertThat(chips).contains(chip.text.toString())
                }
            }
        }
    }
}
