package io.dotanuki.features.facts.ui

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import io.dotanuki.features.facts.R
import io.dotanuki.features.facts.databinding.ViewFactsBinding
import io.dotanuki.features.facts.domain.FactsRetrievalError
import io.dotanuki.features.facts.presentation.ErrorStateResources
import io.dotanuki.features.facts.presentation.FactsPresentation
import io.dotanuki.features.facts.presentation.FactsScreenState
import io.dotanuki.features.facts.presentation.FactsScreenState.Empty
import io.dotanuki.features.facts.presentation.FactsScreenState.Failed
import io.dotanuki.features.facts.presentation.FactsScreenState.Idle
import io.dotanuki.features.facts.presentation.FactsScreenState.Loading
import io.dotanuki.features.facts.presentation.FactsScreenState.Success
import io.dotanuki.platform.android.core.assets.R as assetsR

class FactsView @JvmOverloads constructor(ctx: Context, attrs: AttributeSet? = null) : CoordinatorLayout(ctx, attrs) {

    // Non private for testing purposes
    lateinit var eventsHandler: FactsEventsHandler

    private lateinit var viewBinding: ViewFactsBinding

    fun updateWith(newState: FactsScreenState) {
        when (newState) {
            Idle -> preExecution()
            Loading -> showExecuting()
            Empty -> showEmptyState()
            is Failed -> showErrorState(newState.reason)
            is Success -> showResults(newState.value)
        }

        eventsHandler.postReceive(newState)
    }

    private fun link(handler: FactsEventsHandler, binding: ViewFactsBinding) {
        this.eventsHandler = handler
        this.viewBinding = binding
    }

    private fun preExecution() {
        viewBinding.run {
            errorStateView.visibility = View.GONE
            factsHeadlineLabel.visibility = View.GONE
            factsSwipeToRefresh.setOnRefreshListener { eventsHandler.onRefresh() }
            factsRecyclerView.layoutManager = LinearLayoutManager(context)

            factsToolbar.inflateMenu(R.menu.menu_facts_list)
            factsToolbar.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.menu_item_search_facts) {
                    eventsHandler.onSearch()
                }
                false
            }
        }
    }

    private fun showExecuting() {
        viewBinding.run {
            errorStateView.visibility = View.GONE
            factsSwipeToRefresh.isRefreshing = true
        }
    }

    private fun showResults(presentation: FactsPresentation) {
        viewBinding.run {
            factsSwipeToRefresh.isRefreshing = false
            factsRecyclerView.adapter = FactsRecyclerAdapter(presentation, eventsHandler::onShare)
        }
        showHeadline(presentation.relatedQuery)
    }

    private fun showEmptyState() {
        viewBinding.run {
            factsSwipeToRefresh.isRefreshing = false

            val (errorImage, errorMessage) = ErrorStateResources(FactsRetrievalError.NoResultsFound)

            with(viewBinding) {
                errorStateView.visibility = View.VISIBLE
                errorStateImage.setImageResource(errorImage)
                errorStateLabel.setText(errorMessage)
                retryButton.visibility = View.GONE
            }
        }
    }

    private fun showErrorState(error: Throwable) {

        viewBinding.run {
            factsSwipeToRefresh.isRefreshing = false

            val (errorImage, errorMessage) = ErrorStateResources(error)
            val hasContent = factsRecyclerView.adapter?.let { it.itemCount != 0 } ?: false

            when {
                hasContent -> toast(errorMessage)
                else -> {
                    with(viewBinding) {
                        errorStateView.visibility = View.VISIBLE
                        errorStateImage.setImageResource(errorImage)
                        errorStateLabel.setText(errorMessage)
                        retryButton.setOnClickListener { eventsHandler.onRefresh() }
                    }
                }
            }
        }
    }

    private fun showHeadline(query: String) {

        val highlightColor = ContextCompat.getColor(context, assetsR.color.colorAccent)

        val highlightedFact = SpannableString(query).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, query.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            setSpan(ForegroundColorSpan(highlightColor), 0, query.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        }

        val prefix = resources.getString(R.string.headline_facts)
        val headline = SpannableStringBuilder(prefix).append(" : ").append(highlightedFact)
        viewBinding.factsHeadlineLabel.visibility = View.VISIBLE
        viewBinding.factsHeadlineLabel.text = headline
    }

    private fun toast(errorMessage: Int) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }

    companion object {
        fun create(host: AppCompatActivity, handler: FactsEventsHandler): FactsView {
            val binding = ViewFactsBinding.inflate(host.layoutInflater)
            val rootView = binding.factsViewRoot
            return rootView.apply { link(handler, binding) }
        }
    }
}
