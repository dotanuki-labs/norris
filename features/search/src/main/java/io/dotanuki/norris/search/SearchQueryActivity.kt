package io.dotanuki.norris.search

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import io.dotanuki.logger.Logger
import io.dotanuki.norris.architecture.UserInteraction
import io.dotanuki.norris.architecture.ViewCommand
import io.dotanuki.norris.architecture.ViewState
import io.dotanuki.norris.architecture.ViewState.Failed
import io.dotanuki.norris.architecture.ViewState.FirstLaunch
import io.dotanuki.norris.architecture.ViewState.Loading
import io.dotanuki.norris.architecture.ViewState.Success
import io.dotanuki.norris.features.utilties.selfBind
import io.dotanuki.norris.features.utilties.viewBinding
import io.dotanuki.norris.navigator.Navigator
import io.dotanuki.norris.search.SearchPresentation.QueryValidation
import io.dotanuki.norris.search.SearchPresentation.Suggestions
import io.dotanuki.norris.search.databinding.ActivitySearchQueryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.instance

class SearchQueryActivity : AppCompatActivity(), DIAware {

    override val di by selfBind()

    private val viewBindings by viewBinding(ActivitySearchQueryBinding::inflate)
    private val viewModel by instance<SearchViewModel>()
    private val logger by instance<Logger>()
    private val navigator by instance<Navigator>()

    private var allowedToProceed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBindings.root)
        setup()
    }

    private fun setup() {
        setupTextInputField()
        viewBindings.searchToolbar.setNavigationOnClickListener { finish() }

        viewModel.run {
            lifecycleScope.launch {
                bindToStates().collect { renderState(it) }
            }

            lifecycleScope.launch {
                bindToCommands().collect { executeCommand(it) }
            }
        }
    }

    private fun setupTextInputField() {
        val input = viewBindings.queryTextInput
        input.run {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    proceedIfAllowed(input.editableText.toString())
                }

                return@setOnEditorActionListener false
            }

            addTextChangedListener { input: Editable? ->
                input?.let {
                    viewModel.handle(ValidateQuery(it.toString()))
                }
            }
        }
    }

    private fun renderState(state: ViewState<SearchPresentation>) =
        when (state) {
            is Failed -> handleError(state.reason)
            is Success -> handlePresentation(state.value)
            is Loading.FromEmpty -> startExecution()
            is Loading.FromPrevious -> handlePresentation(state.previous)
            is FirstLaunch -> launch()
        }

    private fun executeCommand(command: ViewCommand) {
        when (command) {
            is ReturnFromSearch -> navigator.returnFromWork()
            else -> throw IllegalArgumentException("Cannot process command -> $command")
        }
    }

    private fun handlePresentation(presentation: SearchPresentation) {
        when (presentation) {
            is Suggestions -> fillChips(presentation)
            is QueryValidation -> renderValidation(presentation)
        }
    }

    private fun renderValidation(validation: QueryValidation) {
        viewBindings.queryTextInput.error =
            when (validation.valid) {
                false -> getString(R.string.error_querytextfield_invalid_query)
                else -> null
            }

        allowedToProceed = validation.valid
    }

    private fun startExecution() {
        viewBindings.loadingSuggestions.visibility = View.VISIBLE
    }

    private fun launch() {
        viewModel.handle(UserInteraction.OpenedScreen)
    }

    private fun fillChips(content: Suggestions) {
        viewBindings.run {
            logger.i("Filling Content")
            recommendationsHeadline.visibility = View.VISIBLE
            historyHeadline.visibility = View.VISIBLE
            loadingSuggestions.visibility = View.GONE

            val (suggestions, history) = content.options

            ChipsGroupPopulator(suggestionChipGroup, R.layout.chip_item_query).run {
                populate(suggestions) { returnQuery(it) }
            }

            ChipsGroupPopulator(historyChipGroup, R.layout.chip_item_query).run {
                populate(history) { returnQuery(it) }
            }
        }
    }

    private fun handleError(reason: Throwable) {
        logger.e("Failed on loading suggestions -> $reason")
        viewBindings.loadingSuggestions.visibility = View.GONE
        showErrorReport(R.string.error_snackbar_cannot_load_suggestions)
    }

    private fun proceedIfAllowed(query: String) {
        if (allowedToProceed) {
            returnQuery(query)
            return
        }

        showErrorReport(R.string.error_snackbar_cannot_proceed)
    }

    private fun returnQuery(query: String) {
        viewModel.handle(
            QueryDefined(query)
        )
    }

    private fun showErrorReport(targetMessageId: Int) {
        viewBindings.run {
            Snackbar
                .make(searchScreenRoot, targetMessageId, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK") { queryTextInput.error = null }
                .show()
        }
    }
}