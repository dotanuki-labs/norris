package io.dotanuki.norris.facts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import io.dotanuki.logger.Logger
import io.dotanuki.norris.architecture.UserInteraction.OpenedScreen
import io.dotanuki.norris.architecture.UserInteraction.RequestedFreshContent
import io.dotanuki.norris.architecture.ViewState
import io.dotanuki.norris.architecture.ViewState.Failed
import io.dotanuki.norris.architecture.ViewState.FirstLaunch
import io.dotanuki.norris.architecture.ViewState.Loading
import io.dotanuki.norris.architecture.ViewState.Success
import io.dotanuki.norris.features.utilties.selfBind
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class FactsActivity : AppCompatActivity(), KodeinAware {

    override val kodein by selfBind()

    private val viewModel by instance<FactsViewModel>()
    private val logger by instance<Logger>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    private fun loadFacts() {
        viewModel.handle(OpenedScreen)
    }

    private fun setup() {
        factsRecyclerView.layoutManager = LinearLayoutManager(this)
        factsSwipeToRefresh.setOnRefreshListener {
            viewModel.handle(RequestedFreshContent)
        }

        lifecycleScope.launch {
            viewModel.bind().collect { renderState(it) }
        }
    }

    private fun renderState(state: ViewState<FactsPresentation>) =
        when (state) {
            is Failed -> reportError(state.reason)
            is Success -> showFacts(state.value)
            is Loading.FromEmpty -> startExecution()
            is Loading.FromPrevious -> showFacts(state.previous)
            is FirstLaunch -> loadFacts()
        }

    private fun showFacts(presentation: FactsPresentation) {
        factsSwipeToRefresh.isRefreshing = false
        factsRecyclerView.adapter = FactsAdapter(presentation) {
        }
    }

    private fun reportError(failed: Throwable) {
        factsSwipeToRefresh.isRefreshing = false
        logger.e("Error -> $failed")

        // TODO : apply better error states
    }

    private fun startExecution() {
        factsSwipeToRefresh.isRefreshing = true
    }
}