package io.dotanuki.features.search.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.dotanuki.features.search.presentation.SearchInteraction
import io.dotanuki.features.search.presentation.SearchViewModel
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private val viewModel by viewModels<SearchViewModel> { TODO() }
    private val eventsHandler by lazy {
        SearchEventsHandler.Unidirectional(viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val searchView = SearchView.create(this, eventsHandler)

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
}
