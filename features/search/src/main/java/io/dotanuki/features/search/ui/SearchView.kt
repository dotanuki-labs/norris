package io.dotanuki.features.search.ui

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import io.dotanuki.features.search.R
import io.dotanuki.features.search.databinding.ViewSearchFactsBinding
import io.dotanuki.features.search.domain.SearchQueryValidation
import io.dotanuki.features.search.presentation.SearchScreenState

class SearchView @JvmOverloads constructor(ctx: Context, attrs: AttributeSet? = null) : CoordinatorLayout(ctx, attrs) {

    lateinit var eventsHandler: SearchEventsHandler
    val receivedStates = mutableListOf<SearchScreenState>()

    private lateinit var viewBinding: ViewSearchFactsBinding
    private val hostActivity by lazy { context as AppCompatActivity }

    fun updateWith(newState: SearchScreenState) {
        Log.d("SearchView", "Received new state -> $newState")
        receivedStates += newState

        when (newState) {
            SearchScreenState.Idle -> setup()
            SearchScreenState.Loading -> showLoading()
            is SearchScreenState.Failed -> showError()
            is SearchScreenState.Success -> showContent(newState.history, newState.suggestions)
            SearchScreenState.Done -> hostActivity.finish()
        }
    }

    private fun link(handler: SearchEventsHandler, binding: ViewSearchFactsBinding) {
        this.eventsHandler = handler
        this.viewBinding = binding
    }

    private fun setup() {
        viewBinding.run {
            searchToolbar.setNavigationOnClickListener { hostActivity.finish() }
            queryTextInput.run {
                addTextChangedListener(
                    afterTextChanged = { current -> validate(current) }
                )

                setOnEditorActionListener { _, actionId, _ ->
                    checkQueryBeforeProceed(actionId)
                    return@setOnEditorActionListener false
                }
            }
        }
    }

    private fun showContent(history: List<String>, suggestions: List<String>) {
        hideLoading()
        viewBinding.run {
            historyHeadline.visibility = View.VISIBLE
            recommendationsHeadline.visibility = View.VISIBLE

            ChipsGroupPopulator(historyChipGroup, R.layout.chip_item_query).run {
                populate(history) { eventsHandler.onChipClicked(it) }
            }

            ChipsGroupPopulator(suggestionChipGroup, R.layout.chip_item_query).run {
                populate(suggestions) { eventsHandler.onChipClicked(it) }
            }
        }
    }

    private fun showError(messageId: Int = R.string.error_snackbar_cannot_load_suggestions) {
        val message = hostActivity.getString(messageId)
        hideLoading()
        viewBinding.run {
            Snackbar
                .make(root, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK") { queryTextInput.error = null }
                .show()
        }
    }

    private fun showLoading() {
        viewBinding.searchSwipeToRefresh.isRefreshing = true
    }

    private fun hideLoading() {
        viewBinding.searchSwipeToRefresh.isRefreshing = false
    }

    private fun TextInputEditText.checkQueryBeforeProceed(actionId: Int) {
        val actual = editableText.toString()

        if (actionId == EditorInfo.IME_ACTION_DONE) {

            if (SearchQueryValidation.validate(actual)) eventsHandler.onNewSearch(actual)
            else showError(R.string.error_snackbar_cannot_proceed)
        }
    }

    private fun TextInputEditText.validate(current: Editable?) {
        current.toString().let {
            error = when {
                it.isEmpty() -> null
                else -> {
                    if (SearchQueryValidation.validate(it)) null
                    else resources.getString(R.string.error_querytextfield_invalid_query)
                }
            }
        }
    }

    companion object {
        fun create(host: AppCompatActivity, handler: SearchEventsHandler): SearchView {
            val binding = ViewSearchFactsBinding.inflate(host.layoutInflater)
            val rootView = binding.searchScreenRoot
            return rootView.apply { link(handler, binding) }
        }
    }
}
