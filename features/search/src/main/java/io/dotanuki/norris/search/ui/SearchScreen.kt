package io.dotanuki.norris.search.ui

import android.text.Editable
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import io.dotanuki.norris.search.R
import io.dotanuki.norris.search.databinding.ActivitySearchBinding
import io.dotanuki.norris.search.domain.SearchQueryValidation
import io.dotanuki.norris.search.presentation.SearchScreenState
import io.dotanuki.norris.search.presentation.SearchScreenState.Content
import io.dotanuki.norris.search.presentation.SearchScreenState.Done
import io.dotanuki.norris.search.presentation.SearchScreenState.Idle
import io.dotanuki.norris.search.presentation.SearchScreenState.Loading

interface SearchScreen {

    interface Delegate {

        fun onChipClicked(term: String)

        fun onQuerySubmited(term: String)
    }

    fun link(host: SearchActivity, delegate: Delegate): View

    fun updateWith(newState: SearchScreenState)
}

internal class WrappedContainer : SearchScreen {

    private lateinit var hostActivity: SearchActivity
    private lateinit var bindings: ActivitySearchBinding
    private lateinit var screenDelegate: SearchScreen.Delegate

    override fun link(host: SearchActivity, delegate: SearchScreen.Delegate): View {
        hostActivity = host
        screenDelegate = delegate
        bindings = ActivitySearchBinding.inflate(hostActivity.layoutInflater)
        return bindings.root
    }

    override fun updateWith(newState: SearchScreenState) {
        when (newState) {
            Idle -> setup()
            Loading -> showLoading()
            is Error -> showError()
            is Content -> showContent(newState.history, newState.suggestions)
            Done -> hostActivity.finish()
        }
    }

    private fun setup() {
        bindings.run {
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
        bindings.run {
            historyHeadline.visibility = View.VISIBLE
            recommendationsHeadline.visibility = View.VISIBLE

            ChipsGroupPopulator(historyChipGroup, R.layout.chip_item_query).run {
                populate(history) { screenDelegate.onChipClicked(it) }
            }

            ChipsGroupPopulator(suggestionChipGroup, R.layout.chip_item_query).run {
                populate(suggestions) { screenDelegate.onChipClicked(it) }
            }
        }
    }

    private fun showError(messageId: Int = R.string.error_snackbar_cannot_load_suggestions) {
        val message = hostActivity.getString(messageId)
        bindings.run {
            Snackbar
                .make(root, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK") { queryTextInput.error = null }
                .show()
        }
    }

    private fun showLoading() {
        bindings.searchSwipeToRefresh.isRefreshing = true
    }

    private fun hideLoading() {
        bindings.searchSwipeToRefresh.isRefreshing = false
    }

    private fun TextInputEditText.checkQueryBeforeProceed(actionId: Int) {
        val actual = editableText.toString()

        if (actionId == EditorInfo.IME_ACTION_DONE) {

            if (SearchQueryValidation.validate(actual)) screenDelegate.onQuerySubmited(actual)
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
}
