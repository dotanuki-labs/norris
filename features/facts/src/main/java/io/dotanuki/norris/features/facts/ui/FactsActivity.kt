package io.dotanuki.norris.features.facts.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.dotanuki.norris.common.android.selfBind
import io.dotanuki.norris.features.facts.presentation.FactsUserInteraction
import io.dotanuki.norris.features.facts.presentation.FactsViewModel
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.instance

class FactsActivity : AppCompatActivity(), DIAware {

    override val di by selfBind()

    private val viewModel by instance<FactsViewModel>()
    private val eventsHandler by instance<FactsEventsHandler>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factsView = FactsView.create(this, eventsHandler)

        setContentView(factsView)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.run {
                    handle(FactsUserInteraction.OpenedScreen)
                    bind().collect { newState ->
                        factsView.updateWith(newState)
                    }
                }
            }
        }
    }
}
