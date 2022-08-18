package io.dotanuki.norris.search.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.dotanuki.norris.common.android.selfBind
import io.dotanuki.norris.search.presentation.SearchInteraction
import io.dotanuki.norris.search.presentation.SearchViewModel
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.instance

class SearchActivity : AppCompatActivity(), DIAware {

    override val di by selfBind()

    private val viewModel by instance<SearchViewModel>()
    private val eventsHandler by instance<SearchEventsHandler>()

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
