package io.dotanuki.norris.facts

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.dotanuki.logger.Logger
import io.dotanuki.norris.architecture.UserInteraction.OpenedScreen
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
        lifecycleScope.launch {
            viewModel.bind().collect { renderState(it) }
        }
    }

    private fun renderState(state: ViewState<FactsPresentation>) =
        when (state) {
            is Failed -> reportError(state.reason)
            is Success -> showFacts(state.value)
            is Loading.FromEmpty -> showLoading()
            is Loading.FromPrevious -> showFacts(state.previous)
            is FirstLaunch -> {
                logger.i("FirstLaunch")
                loadFacts()
            }
        }

    private fun showFacts(presentation: FactsPresentation) {
        logger.i("Success -> $presentation")
        content.text = presentation.facts.first().fact
        loading.visibility = View.INVISIBLE
    }

    private fun reportError(failed: Throwable) {
        logger.e("Error -> $failed")
    }

    private fun showLoading() {
        logger.i("FACTS -> Loading")
        loading.visibility = View.VISIBLE
    }
}
