package io.dotanuki.norris.facts

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import io.dotanuki.norris.features.utilties.toast
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

    private fun refresh() {
        viewModel.handle(RequestedFreshContent)
    }

    private fun setup() {
        factsRecyclerView.layoutManager = LinearLayoutManager(this)
        factsSwipeToRefresh.setOnRefreshListener { refresh() }

        lifecycleScope.launch {
            viewModel.bind().collect { renderState(it) }
        }
    }

    private fun renderState(state: ViewState<FactsPresentation>) =
        when (state) {
            is Failed -> handleError(state.reason)
            is Success -> showFacts(state.value)
            is Loading.FromEmpty -> startExecution()
            is Loading.FromPrevious -> showFacts(state.previous)
            is FirstLaunch -> loadFacts()
        }

    private fun showFacts(presentation: FactsPresentation) {
        factsSwipeToRefresh.isRefreshing = false
        factsRecyclerView.adapter = FactsAdapter(presentation) { shareFact(it) }
    }

    private fun handleError(failed: Throwable) {
        logger.e("Error -> $failed")
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

    private fun showErrorState(errorImage: Int, errorMessage: Int) {
        with(errorStateView) {
            visibility = View.VISIBLE
            errorStateImage.setImageResource(errorImage)
            errorStateLabel.setText(errorMessage)
            retryButton.setOnClickListener { loadFacts() }
        }
    }

    private fun startExecution() {
        errorStateView.visibility = View.GONE
        factsSwipeToRefresh.isRefreshing = true
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