package io.dotanuki.features.search.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.dotanuki.features.search.di.SearchContext
import io.dotanuki.features.search.di.SearchViewModelFactory
import io.dotanuki.features.search.presentation.SearchInteraction
import io.dotanuki.features.search.presentation.SearchViewModel
import kotlinx.coroutines.launch

context (SearchContext)
class SearchActivity :
    AppCompatActivity(),
    SearchEventsHandler {
    private val viewModel by viewModels<SearchViewModel> { SearchViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val searchView = SearchView.create(host = this, handler = this)

        setContentView(searchView)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.run {
                    handle(SearchInteraction.OpenedScreen)
                    bind().collect { newState ->
                        searchView.updateWith(newState)
                    }
                }
            }
        }
    }

    override fun onNewSearch(term: String) {
        viewModel.handle(SearchInteraction.NewQuerySet(term))
    }

    override fun onChipClicked(term: String) {
        viewModel.handle(SearchInteraction.NewQuerySet(term))
    }
}
