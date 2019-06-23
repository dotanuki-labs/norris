package io.dotanuki.norris.facts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.dotanuki.logger.Logger
import io.dotanuki.norris.architecture.UserInteraction
import io.dotanuki.norris.architecture.ViewState
import io.dotanuki.norris.architecture.ViewState.Failed
import io.dotanuki.norris.architecture.ViewState.Loading
import io.dotanuki.norris.architecture.ViewState.Success
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class FactsActivity : AppCompatActivity(), KodeinAware {

    private val launcher = MainScope()

    override val kodein by selfBind {
        bind() from provider {
            launcher
        }
    }

    private val viewModel by instance<FactsViewModel>()
    private val logger by instance<Logger>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    override fun onDestroy() {
        launcher.cancel()
        super.onDestroy()
    }

    private fun setup() {
        launcher.launch {
            viewModel.bind().collect { renderState(it) }
        }

        viewModel.handle(
            UserInteraction.OpenedScreen
        )
    }

    private fun renderState(state: ViewState<List<FactPresentation>>) =
        when (state) {
            is Loading -> showLoading()
            is Failed -> reportError(state.failed)
            is Success -> showFacts(state.value)
        }

    private fun showFacts(value: List<FactPresentation>) {
        logger.i("Success -> $value")
    }

    private fun reportError(failed: Throwable) {
        logger.e("Error -> $failed")
    }

    private fun showLoading() {
        logger.i("FACTS -> Loading")
    }
}
