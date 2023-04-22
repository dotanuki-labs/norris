package io.dotanuki.features.facts.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.dotanuki.features.facts.presentation.FactsUserInteraction
import io.dotanuki.features.facts.presentation.FactsViewModel
import io.dotanuki.platform.android.core.navigator.Navigator
import kotlinx.coroutines.launch

class FactsActivity : AppCompatActivity() {

    private val viewModel by viewModels<FactsViewModel> { TODO() }
    private val navigator : Navigator = TODO()
    private val eventsHandler by lazy {
        FactsEventsHandler.Unidirectional(viewModel, navigator)
    }

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
