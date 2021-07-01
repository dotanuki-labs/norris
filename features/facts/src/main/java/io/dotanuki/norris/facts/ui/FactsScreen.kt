package io.dotanuki.norris.facts.ui

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import io.dotanuki.norris.facts.R
import io.dotanuki.norris.facts.databinding.ActivityFactsBinding
import io.dotanuki.norris.facts.domain.FactsRetrievalError.NoResultsFound
import io.dotanuki.norris.facts.presentation.ErrorStateResources
import io.dotanuki.norris.facts.presentation.FactDisplayRow
import io.dotanuki.norris.facts.presentation.FactsPresentation
import io.dotanuki.norris.sharedassets.R as sharedR

class FactsScreen(
    private val delegate: Delegate
) {

    interface Delegate {

        val binding: ActivityFactsBinding

        fun onRefresh()

        fun onSearch()

        fun onShare(row: FactDisplayRow)
    }

    private val context by lazy {
        delegate.binding.root.context
    }

    fun setup() {
        delegate.binding.run {
            factsRecyclerView.layoutManager = LinearLayoutManager(context)
            factsSwipeToRefresh.setOnRefreshListener { delegate.onRefresh() }

            factsToolbar.inflateMenu(R.menu.menu_facts_list)
            factsToolbar.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.menu_item_search_facts) {
                    delegate.onSearch()
                }
                false
            }
        }
    }

    fun preExecution() {
        delegate.binding.run {
            errorStateView.visibility = View.GONE
            factsHeadlineLabel.visibility = View.GONE
        }
    }

    fun showExecuting() {
        delegate.binding.run {
            errorStateView.visibility = View.GONE
            factsSwipeToRefresh.isRefreshing = true
        }
    }

    fun showResults(presentation: FactsPresentation) {
        delegate.binding.run {
            factsSwipeToRefresh.isRefreshing = false
            factsRecyclerView.adapter = FactsRecyclerAdapter(presentation, delegate::onShare)
        }
        showHeadline(presentation.relatedQuery)
    }

    fun showEmptyState() {
        delegate.binding.run {
            factsSwipeToRefresh.isRefreshing = false

            val (errorImage, errorMessage) = ErrorStateResources(NoResultsFound)

            with(delegate.binding) {
                errorStateView.visibility = View.VISIBLE
                errorStateImage.setImageResource(errorImage)
                errorStateLabel.setText(errorMessage)
                retryButton.visibility = View.GONE
            }
        }
    }

    fun showErrorState(error: Throwable) {

        delegate.binding.run {
            factsSwipeToRefresh.isRefreshing = false

            val (errorImage, errorMessage) = ErrorStateResources(error)
            val hasContent = factsRecyclerView.adapter?.let { it.itemCount != 0 } ?: false

            when {
                hasContent -> toast(errorMessage)
                else -> {
                    with(delegate.binding) {
                        errorStateView.visibility = View.VISIBLE
                        errorStateImage.setImageResource(errorImage)
                        errorStateLabel.setText(errorMessage)
                        retryButton.setOnClickListener { delegate.onRefresh() }
                    }
                }
            }
        }
    }

    private fun showHeadline(query: String) {

        val highlightColor = ContextCompat.getColor(context, sharedR.color.colorAccent)

        val highlightedFact = SpannableString(query).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, query.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            setSpan(
                ForegroundColorSpan(highlightColor), 0, query.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }

        val prefix = context.getString(R.string.headline_facts)
        val headline = SpannableStringBuilder(prefix).append(" : ").append(highlightedFact)
        delegate.binding.factsHeadlineLabel.visibility = View.VISIBLE
        delegate.binding.factsHeadlineLabel.text = headline
    }

    private fun toast(errorMessage: Int) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }
}
