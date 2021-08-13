package io.dotanuki.norris.search.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import io.dotanuki.norris.common.android.repeatOnLifecycle
import io.dotanuki.norris.common.android.selfBind
import io.dotanuki.norris.search.presentation.SearchInteraction
import io.dotanuki.norris.search.presentation.SearchViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.instance

class SearchActivity : AppCompatActivity(), DIAware {

    override val di by selfBind()
    private val viewModel by instance<SearchViewModel>()
    private val searchScreen by instance<SearchScreen>()

    private val delegate by lazy {
        object : SearchScreen.Delegate {
            override fun onNewSearch(term: String) {
                viewModel.handle(SearchInteraction.NewQuerySet(term))
            }

            override fun onChipClicked(term: String) {
                viewModel.handle(SearchInteraction.NewQuerySet(term))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootView = searchScreen.link(this, delegate)
        setContentView(rootView)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.run {
                    handle(SearchInteraction.OpenedScreen)
                    bind().collect { newState ->
                        searchScreen.updateWith(newState)
                    }
                }
            }
        }
    }
}
