package io.dotanuki.demos.norris.facts

import androidx.test.espresso.Espresso.onView
import com.schibsted.spain.barista.internal.matcher.DisplayedMatchers.displayedWithId
import io.dotanuki.demos.norris.R
import io.dotanuki.demos.norris.dsl.ErrorStateImageChecker
import io.dotanuki.demos.norris.dsl.ErrorStateLabelChecker
import io.dotanuki.demos.norris.dsl.FactsContent
import io.dotanuki.demos.norris.dsl.RefreshingStateChecker
import io.dotanuki.demos.norris.dsl.ViewVisibilityChecker
import io.dotanuki.demos.norris.util.RecyclerViewContentAssertion

fun factsListChecks(block: FactsListChecker.() -> Unit) = FactsListChecker().apply { block() }

class FactsListChecker {
    val loadingIndicator = RefreshingStateChecker(R.id.factsSwipeToRefresh)
    val errorState = ViewVisibilityChecker(R.id.errorStateView)
    val errorStateImage = ErrorStateImageChecker(R.id.errorStateImage)
    val errorStateLabel = ErrorStateLabelChecker(R.id.errorStateLabel)
    val content = FactListsSimpleChecker(R.id.factsRecyclerView)
}

class FactListsSimpleChecker(private val factsRecyclerView: Int) {
    infix fun shouldBe(content: FactsContent) {

        val hasContent = when (content) {
            FactsContent.PRESENT -> true
            FactsContent.ABSENT -> false
        }

        onView(displayedWithId(factsRecyclerView))
            .check(RecyclerViewContentAssertion(hasContent))
    }
}