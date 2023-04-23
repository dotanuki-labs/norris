package io.dotanuki.features.facts.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.dotanuki.features.facts.di.FactsViewModelFactory
import io.dotanuki.features.facts.presentation.FactsUserInteraction
import io.dotanuki.features.facts.presentation.FactsViewModel
import io.dotanuki.platform.android.core.navigator.Navigator.Companion.retrieveNavigator
import io.dotanuki.platform.android.core.navigator.Screen
import kotlinx.coroutines.launch

class FactsActivity(vmFactory: FactsViewModelFactory) : AppCompatActivity(), FactsEventsHandler {

    private val viewModel by viewModels<FactsViewModel> { vmFactory }
    private val navigator by lazy { retrieveNavigator() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factsView = FactsView.create(this, this)

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

    override fun onRefresh() {
        viewModel.handle(FactsUserInteraction.RequestedFreshContent)
    }

    override fun onSearch() {
        navigator.navigateTo(Screen.SearchQuery)
    }

    override fun onShare(fact: String) {
        navigator.toSharingApp(fact, "Share this fact!")
    }
}
