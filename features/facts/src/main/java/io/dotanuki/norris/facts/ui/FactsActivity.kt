package io.dotanuki.norris.facts.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import io.dotanuki.logger.Logger
import io.dotanuki.norris.facts.databinding.ActivityFactsBinding
import io.dotanuki.norris.facts.presentation.FactDisplayRow
import io.dotanuki.norris.facts.presentation.FactsScreenState
import io.dotanuki.norris.facts.presentation.FactsScreenState.Empty
import io.dotanuki.norris.facts.presentation.FactsScreenState.Failed
import io.dotanuki.norris.facts.presentation.FactsScreenState.Idle
import io.dotanuki.norris.facts.presentation.FactsScreenState.Loading
import io.dotanuki.norris.facts.presentation.FactsScreenState.Success
import io.dotanuki.norris.facts.presentation.FactsUserInteraction.OpenedScreen
import io.dotanuki.norris.facts.presentation.FactsViewModel
import io.dotanuki.norris.facts.presentation.repeatOnLifecycle
import io.dotanuki.norris.features.utilties.selfBind
import io.dotanuki.norris.features.utilties.viewBinding
import io.dotanuki.norris.navigator.Navigator
import io.dotanuki.norris.navigator.Screen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.instance

class FactsActivity : AppCompatActivity(), DIAware {

    override val di by selfBind()

    private val viewBindings by viewBinding(ActivityFactsBinding::inflate)
    private val viewModel by instance<FactsViewModel>()
    private val logger by instance<Logger>()
    private val navigator by instance<Navigator>()

    private val viewDelegate by lazy {
        FactsViewDelegate(viewBindings, this::loadFacts, this::shareFact, this::goToSearch)
    }

    var actualState: FactsScreenState = Idle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBindings.root)
        setup()
    }

    private fun setup() {
        viewDelegate.setup()

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                loadFacts()
                viewModel.bind().collect { renderState(it) }
            }
        }
    }

    private fun loadFacts() {
        logger.v("Requesting content ...")
        viewModel.handle(OpenedScreen)
    }

    private fun renderState(state: FactsScreenState) =
        with(viewDelegate) {
            when (state) {
                Idle -> preExecution()
                Loading -> showExecuting()
                Empty -> showEmptyState()
                is Failed -> showErrorState(state.reason)
                is Success -> showResults(state.value)
            }.also {
                actualState = state
            }
        }

    private fun goToSearch() {
        navigator.navigateTo(Screen.SearchQuery)
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
