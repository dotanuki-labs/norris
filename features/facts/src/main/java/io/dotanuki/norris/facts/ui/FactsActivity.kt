package io.dotanuki.norris.facts.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import io.dotanuki.norris.facts.presentation.FactsUserInteraction
import io.dotanuki.norris.facts.presentation.FactsUserInteraction.RequestedFreshContent
import io.dotanuki.norris.facts.presentation.FactsViewModel
import io.dotanuki.norris.features.utilties.repeatOnLifecycle
import io.dotanuki.norris.features.utilties.selfBind
import io.dotanuki.norris.navigator.Navigator
import io.dotanuki.norris.navigator.Screen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.instance

class FactsActivity : AppCompatActivity(), DIAware {

    override val di by selfBind()

    private val viewModel by instance<FactsViewModel>()
    private val navigator by instance<Navigator>()
    private val factsScreen by instance<FactsScreen>()

    private val delegate by lazy {
        object : FactsScreen.Delegate {
            override fun onRefresh() {
                viewModel.handle(RequestedFreshContent)
            }

            override fun onSearch() {
                navigator.navigateTo(Screen.SearchQuery)
            }

            override fun onShare(url: String) {
                navigator.toSharingApp(url, "Share this fact!")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootView = factsScreen.link(this, delegate)
        setContentView(rootView)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.run {
                    handle(FactsUserInteraction.OpenedScreen)
                    bind().collect { newState ->
                        factsScreen.updateWith(newState)
                    }
                }
            }
        }
    }
}
