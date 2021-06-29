package io.dotanuki.norris.search.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.dotanuki.logger.Logger
import io.dotanuki.norris.features.utilties.selfBind
import io.dotanuki.norris.features.utilties.viewBinding
import io.dotanuki.norris.search.R
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
import kotlin.properties.Delegates

class SearchActivity : AppCompatActivity(), DIAware {

    override val di by selfBind()

    private val viewBindings by viewBinding(ActivitySearchBinding::inflate)
    private val viewModel by instance<SearchViewModel>()
    private val logger by instance<Logger>()

    private val viewDelegate by lazy {
        SearchViewDelegate(
            binding = viewBindings,
            onUpNavigationClicked = this::finish,
            onChipClicked = this::onChipClicked,
            onQuerySubmited = this::onQuerySubmited
        )
    }

    var actualState by Delegates.notNull<SearchScreenState>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBindings.root)
        setup()
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
        logger.e("New state -> $state")
        actualState = state

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

    private fun onQuerySubmited(query: String) {
        viewModel.handle(SearchInteraction.NewQuerySet(query))
    }

    private fun onChipClicked(query: String) {
        viewModel.handle(SearchInteraction.NewQuerySet(query))
    }

    private fun handleError(reason: Throwable) {
        logger.e("Failed on loading suggestions -> $reason")
        val message = getString(R.string.error_snackbar_cannot_load_suggestions)
        viewDelegate.showError(message)
    }
}
