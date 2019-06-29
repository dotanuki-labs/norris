package io.dotanuki.norris.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.dotanuki.logger.Logger
import io.dotanuki.norris.architecture.UserInteraction
import io.dotanuki.norris.architecture.ViewState
import io.dotanuki.norris.architecture.ViewState.Failed
import io.dotanuki.norris.architecture.ViewState.FirstLaunch
import io.dotanuki.norris.architecture.ViewState.Loading
import io.dotanuki.norris.architecture.ViewState.Success
import io.dotanuki.norris.features.utilties.selfBind
import io.dotanuki.norris.features.utilties.toast
import kotlinx.android.synthetic.main.activity_search_query.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class SearchQueryActivity : AppCompatActivity(), KodeinAware {

    override val kodein by selfBind()

    private val viewModel by instance<SearchViewModel>()
    private val logger by instance<Logger>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_query)
        setup()
    }

    private fun setup() {
        lifecycleScope.launch {
            viewModel.bind().collect { renderState(it) }
        }

        queryTextInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchForQuery(
                    queryTextInput.editableText.toString()
                )
            }
            return@setOnEditorActionListener false
        }

        searchToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun renderState(state: ViewState<SearchPresentation>) =
        when (state) {
            is Failed -> handleError(state.reason)
            is Success -> fillChips(state.value)
            is Loading.FromEmpty -> startExecution()
            is Loading.FromPrevious -> fillChips(state.previous)
            is FirstLaunch -> launch()
        }

    private fun startExecution() {
        loadingSuggestions.visibility = View.VISIBLE
    }

    private fun launch() {
        viewModel.handle(UserInteraction.OpenedScreen)
    }

    private fun fillChips(search: SearchPresentation) {
        loadingSuggestions.visibility = View.GONE

        val (suggestions, history) = search.options

        ChipsGroupPopulator(suggestionChipGroup, R.layout.chip_item_query).run {
            populate(suggestions) {
                searchForQuery(it)
            }
        }

        ChipsGroupPopulator(historyChipGroup, R.layout.chip_item_query).run {
            populate(history) {
                searchForQuery(it)
            }
        }
    }

    private fun handleError(reason: Throwable) {
        logger.e("Failed on loading suggestions -> $reason")
        loadingSuggestions.visibility = View.GONE
    }

    private fun searchForQuery(query: String) {
        toast(query)
    }
}