package io.dotanuki.norris.search.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.dotanuki.logger.Logger
import io.dotanuki.norris.features.utilties.selfBind
import io.dotanuki.norris.features.utilties.viewBinding
import io.dotanuki.norris.search.databinding.ActivitySearchBinding
import io.dotanuki.norris.search.presentation.SearchInteraction
import io.dotanuki.norris.search.presentation.SearchScreenState
import io.dotanuki.norris.search.presentation.SearchScreenState.Content
import io.dotanuki.norris.search.presentation.SearchScreenState.Done
import io.dotanuki.norris.search.presentation.SearchScreenState.Error
import io.dotanuki.norris.search.presentation.SearchScreenState.Idle
import io.dotanuki.norris.search.presentation.SearchScreenState.Loading
import io.dotanuki.norris.search.presentation.SearchViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.instance

class SearchActivity : AppCompatActivity(), DIAware, SearchViewDelegate.Callbacks {

    override val di by selfBind()

    private val viewBindings by viewBinding(ActivitySearchBinding::inflate)
    private val viewModel by instance<SearchViewModel>()
    private val logger by instance<Logger>()

    private val viewDelegate by lazy {
        SearchViewDelegate(viewBindings, this)
    }

    val states by lazy {
        mutableListOf<SearchScreenState>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBindings.root)
        setup()
    }

    override fun onQuerySubmited(term: String) {
        viewModel.handle(SearchInteraction.NewQuerySet(term))
    }

    override fun onUpNavigationClicked() {
        finish()
    }

    override fun onChipClicked(term: String) {
        viewModel.handle(SearchInteraction.NewQuerySet(term))
    }

    private fun setup() {
        viewDelegate.setup()

        viewModel.run {
            lifecycleScope.launch {
                bind().collect { renderState(it) }
            }
        }
    }

    private fun renderState(state: SearchScreenState) {
        states += state

        with(viewDelegate) {
            when (state) {
                Idle -> launch()
                Loading -> showLoading()
                is Error -> handleError(state.error)
                is Content -> showContent(state.history, state.suggestions)
                Done -> finish()
            }
        }
    }

    private fun launch() {
        viewModel.handle(SearchInteraction.OpenedScreen)
    }

    private fun handleError(reason: Throwable) {
        logger.e("Failed on loading suggestions -> $reason")
        viewDelegate.showError()
    }
}
