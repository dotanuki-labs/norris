package io.dotanuki.norris.facts

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable.SPAN_EXCLUSIVE_INCLUSIVE
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import io.dotanuki.logger.Logger
import io.dotanuki.norris.facts.FactsUserInteraction.*
import io.dotanuki.norris.facts.databinding.ActivityFactsBinding
import io.dotanuki.norris.features.utilties.selfBind
import io.dotanuki.norris.features.utilties.toast
import io.dotanuki.norris.features.utilties.viewBinding
import io.dotanuki.norris.navigator.DefineSearchQuery
import io.dotanuki.norris.navigator.HandleDelegatedWork
import io.dotanuki.norris.navigator.Navigator
import io.dotanuki.norris.navigator.PostFlow.NoResults
import io.dotanuki.norris.navigator.PostFlow.WithResults
import io.dotanuki.norris.navigator.Screen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.instance
import io.dotanuki.norris.sharedassets.R as SharedR

@Suppress("TooManyFunctions")
class FactsActivity : AppCompatActivity(), DIAware {

    override val di by selfBind()

    private val viewBindings by viewBinding(ActivityFactsBinding::inflate)
    private val viewModel by instance<FactsViewModel>()
    private val logger by instance<Logger>()
    private val navigator by instance<Navigator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBindings.root)
        setup()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_facts_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        item.let {
            when (it.itemId) {
                R.id.menu_item_search_facts -> goToSearch()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (HandleDelegatedWork(requestCode, resultCode, data, DefineSearchQuery)) {
            NoResults -> logger.i("No query terms returned")
            is WithResults -> refresh()
        }
    }

    private fun goToSearch() {
        navigator.requestWork(Screen.SearchQuery, DefineSearchQuery)
    }

    private fun loadFacts() {
        viewModel.handle(OpenedScreen)
    }

    private fun refresh() {
        viewModel.handle(RequestedFreshContent)
    }

    private fun setup() {
        viewBindings.run {
            setSupportActionBar(factsToolbar)
            factsRecyclerView.layoutManager = LinearLayoutManager(this@FactsActivity)
            factsSwipeToRefresh.setOnRefreshListener { refresh() }
        }

        lifecycleScope.launch {
            viewModel.bind().collect { renderState(it) }
        }
    }

    private fun renderState(state: FactsScreenState) =
        when (state) {
            is FactsScreenState.Failed -> handleError(state.reason)
            is FactsScreenState.Success -> showFacts(state.value)
            is FactsScreenState.Loading -> startExecution()
            is FactsScreenState.Idle -> loadFacts()
        }

    private fun showFacts(presentation: FactsPresentation) {
        viewBindings.run {
            factsSwipeToRefresh.isRefreshing = false
            factsRecyclerView.adapter = FactsAdapter(presentation) { shareFact(it) }
        }
        showHeadline(presentation.relatedQuery)
    }

    private fun showHeadline(query: String) {

        val highlightColor = ContextCompat.getColor(this, SharedR.color.colorAccent)

        val highlightedFact = SpannableString(query).apply {
            setSpan(StyleSpan(Typeface.BOLD), 0, query.length, SPAN_EXCLUSIVE_INCLUSIVE)
            setSpan(ForegroundColorSpan(highlightColor), 0, query.length, SPAN_EXCLUSIVE_INCLUSIVE)
        }

        val prefix = getString(R.string.headline_facts)
        val headline = SpannableStringBuilder(prefix).append(" : ").append(highlightedFact)
        viewBindings.factsHeadlineLabel.text = headline
    }

    private fun handleError(failed: Throwable) {
        logger.e("Error -> $failed")

        viewBindings.run {
            factsSwipeToRefresh.isRefreshing = false

            val (errorImage, errorMessage) = ErrorStateResources(failed)
            val hasPreviousContent =
                factsRecyclerView.adapter
                    ?.let { it.itemCount != 0 }
                    ?: false

            when {
                hasPreviousContent -> toast(errorMessage)
                else -> showErrorState(errorImage, errorMessage)
            }
        }
    }

    private fun showErrorState(errorImage: Int, errorMessage: Int) {
        with(viewBindings) {
            errorStateView.visibility = View.VISIBLE
            errorStateImage.setImageResource(errorImage)
            errorStateLabel.setText(errorMessage)
            retryButton.setOnClickListener { loadFacts() }
        }
    }

    private fun startExecution() {
        viewBindings.run {
            errorStateView.visibility = View.GONE
            factsSwipeToRefresh.isRefreshing = true
        }
    }

    private fun shareFact(row: FactDisplayRow) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, row.url)
            type = "text/plain"
        }

        startActivity(
            Intent.createChooser(sendIntent, "Share this Chuck Norris Fact")
        )
    }
}