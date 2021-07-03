package io.dotanuki.norris.facts.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import io.dotanuki.norris.facts.databinding.ActivityFactsBinding
import io.dotanuki.norris.facts.presentation.FactDisplayRow
import io.dotanuki.norris.facts.presentation.FactsScreenState
import io.dotanuki.norris.facts.presentation.FactsScreenState.Empty
import io.dotanuki.norris.facts.presentation.FactsScreenState.Failed
import io.dotanuki.norris.facts.presentation.FactsScreenState.Idle
import io.dotanuki.norris.facts.presentation.FactsScreenState.Loading
import io.dotanuki.norris.facts.presentation.FactsScreenState.Success
import io.dotanuki.norris.facts.presentation.FactsUserInteraction.OpenedScreen
import io.dotanuki.norris.facts.presentation.FactsUserInteraction.RequestedFreshContent
import io.dotanuki.norris.facts.presentation.FactsViewModel
import io.dotanuki.norris.features.utilties.repeatOnLifecycle
import io.dotanuki.norris.features.utilties.selfBind
import io.dotanuki.norris.features.utilties.viewBinding
import io.dotanuki.norris.navigator.Navigator
import io.dotanuki.norris.navigator.Screen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.instance

class FactsActivity : AppCompatActivity(), FactsScreen.Delegate, DIAware {

    private val viewModel by instance<FactsViewModel>()
    private val navigator by instance<Navigator>()

    private val factsScreen by lazy {
        FactsScreen(this)
    }

    val states by lazy {
        mutableListOf<FactsScreenState>()
    }

    override val di by selfBind()

    override val binding by viewBinding(ActivityFactsBinding::inflate)

    override fun onRefresh() {
        viewModel.handle(RequestedFreshContent)
    }

    override fun onSearch() {
        navigator.navigateTo(Screen.SearchQuery)
    }

    override fun onShare(row: FactDisplayRow) {
        val share = SharingHelper.createChooser(row.url)
        startActivity(share)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setup()
    }

    private fun setup() {
        factsScreen.setup()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.run {
                    handle(OpenedScreen)
                    bind().collect { renderState(it) }
                }
            }
        }
    }

    private fun renderState(state: FactsScreenState) =
        with(factsScreen) {
            when (state) {
                Idle -> preExecution()
                Loading -> showExecuting()
                Empty -> showEmptyState()
                is Failed -> showErrorState(state.reason)
                is Success -> showResults(state.value)
            }.also {
                states += state
            }
        }
}
