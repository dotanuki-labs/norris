package io.dotanuki.norris.search

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import io.dotanuki.logger.Logger
import io.dotanuki.norris.features.utilties.selfBind
import io.dotanuki.norris.features.utilties.viewBinding
import io.dotanuki.norris.search.SearchInteraction.QueryDefined
import io.dotanuki.norris.search.SearchScreenState.Companion.FINAL
import io.dotanuki.norris.search.SearchScreenState.Companion.INITIAL
import io.dotanuki.norris.search.SearchScreenState.Companion.WRONG_QUERY
import io.dotanuki.norris.search.SearchScreenState.Recommendations
import io.dotanuki.norris.search.SearchScreenState.SearchHistory
import io.dotanuki.norris.search.SearchScreenState.SearchQuery
import io.dotanuki.norris.search.databinding.ActivitySearchQueryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.instance

class SearchQueryActivity : AppCompatActivity(), DIAware {

    override val di by selfBind()

    private val viewBindings by viewBinding(ActivitySearchQueryBinding::inflate)
    private val viewModel by instance<SearchViewModel>()
    private val logger by instance<Logger>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBindings.root)
        setup()
    }

    private fun setup() {
        setupTextInputField()
        viewBindings.searchToolbar.setNavigationOnClickListener { finish() }

        viewModel.run {
            lifecycleScope.launch {
                bind().collect { renderState(it) }
            }
        }
    }

    private fun setupTextInputField() {
        val input = viewBindings.queryTextInput
        input.run {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.handle(
                        QueryDefined(input.text.toString())
                    )
                }

                return@setOnEditorActionListener false
            }

            addTextChangedListener { input: Editable? ->
                input?.let {
                    viewModel.handle(SearchInteraction.QueryFieldChanged(it.toString()))
                }
            }
        }
    }

    private fun renderState(state: SearchScreenState) {
        when (state) {
            INITIAL -> launch()
            WRONG_QUERY -> describeErrorToUser(R.string.error_querytextfield_invalid_query)
            FINAL -> finish()
            else -> {
                renderQueryState(state.searchQuery)
                renderRecommendationsState(state.recommendations)
                renderSearchHistoryState(state.searchHistory)
            }
        }
    }

    private fun renderSearchHistoryState(subState: SearchHistory) {
        when (subState) {
            SearchHistory.Idle -> logger.d("Substate on Idle")
            SearchHistory.Loading -> showLoading()
            is SearchHistory.Failed -> {
                hideLoading()
                handleError(subState.error)
            }
            is SearchHistory.Success -> {
                hideLoading()
                viewBindings.run {
                    historyHeadline.visibility = View.VISIBLE
                    ChipsGroupPopulator(historyChipGroup, R.layout.chip_item_query).run {
                        populate(subState.items) {
                            handleSelection(it)
                        }
                    }
                }
            }
        }
    }

    private fun renderRecommendationsState(subState: Recommendations) {
        when (subState) {
            Recommendations.Idle -> logger.d("Substate on Idle")
            Recommendations.Loading -> showLoading()
            is Recommendations.Failed -> {
                hideLoading()
                handleError(subState.error)
            }
            is Recommendations.Success -> {
                hideLoading()
                viewBindings.run {
                    historyHeadline.visibility = View.VISIBLE
                    ChipsGroupPopulator(suggestionChipGroup, R.layout.chip_item_query).run {
                        populate(subState.items) { handleSelection(it) }
                    }
                }
            }
        }
    }

    private fun handleSelection(query: String) {
        viewModel.handle(
            SearchInteraction.QueryFieldChanged(query)
        )
    }

    private fun renderQueryState(substate: SearchQuery) {
        if (substate == SearchQuery.NOT_SET) return

        viewBindings.queryTextInput.error =
            when (substate) {
                SearchQuery.INVALID -> getString(R.string.error_querytextfield_invalid_query)
                else -> null
            }
    }

    private fun showLoading() {
        viewBindings.searchSwipeToRefresh.isRefreshing = true
    }

    private fun hideLoading() {
        viewBindings.searchSwipeToRefresh.isRefreshing = false
    }

    private fun launch() {
        viewModel.handle(SearchInteraction.OpenedScreen)
    }

    private fun handleError(reason: Throwable) {
        logger.e("Failed on loading suggestions -> $reason")
        describeErrorToUser(R.string.error_snackbar_cannot_load_suggestions)
    }

    private fun describeErrorToUser(targetMessageId: Int) {
        viewBindings.run {
            Snackbar
                .make(searchScreenRoot, targetMessageId, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK") { queryTextInput.error = null }
                .show()
        }
    }
}
