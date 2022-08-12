package io.dotanuki.norris.facts.ui

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import io.dotanuki.norris.facts.R
import io.dotanuki.norris.facts.databinding.ActivityFactsBinding
import io.dotanuki.norris.facts.domain.FactsRetrievalError.NoResultsFound
import io.dotanuki.norris.facts.presentation.ErrorStateResources
import io.dotanuki.norris.facts.presentation.FactsPresentation
import io.dotanuki.norris.facts.presentation.FactsScreenState
import io.dotanuki.norris.facts.presentation.FactsScreenState.Empty
import io.dotanuki.norris.facts.presentation.FactsScreenState.Failed
import io.dotanuki.norris.facts.presentation.FactsScreenState.Idle
import io.dotanuki.norris.facts.presentation.FactsScreenState.Loading
import io.dotanuki.norris.facts.presentation.FactsScreenState.Success
import io.dotanuki.norris.sharedassets.R as sharedR

interface FactsScreen {

    interface Delegate {

        fun onRefresh()

        fun onSearch()

        fun onShare(fact: String)
    }

    fun link(host: AppCompatActivity, delegate: Delegate): View

    fun updateWith(newState: FactsScreenState)
}

class WrappedContainer : FactsScreen {

    private lateinit var hostActivity: AppCompatActivity
    private lateinit var bindings: ActivityFactsBinding
    private lateinit var screenDelegate: FactsScreen.Delegate

    override fun updateWith(newState: FactsScreenState) {
        when (newState) {
            Idle -> preExecution()
            Loading -> showExecuting()
            Empty -> showEmptyState()
            is Failed -> showErrorState(newState.reason)
            is Success -> showResults(newState.value)
        }
    }

    override fun link(host: AppCompatActivity, delegate: FactsScreen.Delegate): View {
        hostActivity = host
        screenDelegate = delegate
        bindings = ActivityFactsBinding.inflate(hostActivity.layoutInflater)
        return bindings.root
    }

    private fun preExecution() {
        bindings.run {
            errorStateView.visibility = View.GONE
            factsHeadlineLabel.visibility = View.GONE
            factsSwipeToRefresh.setOnRefreshListener { screenDelegate.onRefresh() }
            factsRecyclerView.layoutManager = LinearLayoutManager(hostActivity)

            factsToolbar.inflateMenu(R.menu.menu_facts_list)
            factsToolbar.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.menu_item_search_facts) {
                    screenDelegate.onSearch()
                }
                false
            }
        }
    }

    private fun showExecuting() {
        bindings.run {
            errorStateView.visibility = View.GONE
            factsSwipeToRefresh.isRefreshing = true
        }
    }

    private fun showResults(presentation: FactsPresentation) {
        bindings.run {
            factsSwipeToRefresh.isRefreshing = false
            factsRecyclerView.adapter = FactsRecyclerAdapter(presentation, screenDelegate::onShare)
        }
        showHeadline(presentation.relatedQuery)
    }

    private fun showEmptyState() {
        bindings.run {
            factsSwipeToRefresh.isRefreshing = false

            val (errorImage, errorMessage) = ErrorStateResources(NoResultsFound)

            with(bindings) {
                errorStateView.visibility = View.VISIBLE
                errorStateImage.setImageResource(errorImage)
                errorStateLabel.setText(errorMessage)
                retryButton.visibility = View.GONE
            }
        }
    }

    private fun showErrorState(error: Throwable) {

        bindings.run {
            factsSwipeToRefresh.isRefreshing = false

            val (errorImage, errorMessage) = ErrorStateResources(error)
            val hasContent = factsRecyclerView.adapter?.let { it.itemCount != 0 } ?: false

            when {
                hasContent -> toast(errorMessage)
                else -> {
                    with(bindings) {
                        errorStateView.visibility = View.VISIBLE
                        errorStateImage.setImageResource(errorImage)
                        errorStateLabel.setText(errorMessage)
                        retryButton.setOnClickListener { screenDelegate.onRefresh() }
                    }
                }
            }
        }
    }

    private fun showHeadline(query: String) {

        val highlightColor = ContextCompat.getColor(hostActivity, sharedR.color.colorAccent)

        val highlightedFact = SpannableString(query).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, query.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            setSpan(ForegroundColorSpan(highlightColor), 0, query.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        }

        val prefix = hostActivity.getString(R.string.headline_facts)
        val headline = SpannableStringBuilder(prefix).append(" : ").append(highlightedFact)
        bindings.factsHeadlineLabel.visibility = View.VISIBLE
        bindings.factsHeadlineLabel.text = headline
    }

    private fun toast(errorMessage: Int) {
        Toast.makeText(hostActivity, errorMessage, Toast.LENGTH_LONG).show()
    }
}
