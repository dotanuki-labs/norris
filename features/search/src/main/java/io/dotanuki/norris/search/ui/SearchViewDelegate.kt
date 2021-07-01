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

class SearchViewDelegate(
    private val binding: ActivitySearchBinding,
    private val callbacks: Callbacks,
) {

    private val context by lazy {
        binding.root.context
    }

    interface Callbacks {
        fun onUpNavigationClicked()

        fun onChipClicked(term: String)

        fun onQuerySubmited(term: String)
    }

    fun setup() {
        binding.run {
            searchToolbar.setNavigationOnClickListener { callbacks.onUpNavigationClicked() }
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

    fun showContent(history: List<String>, suggestions: List<String>) {
        hideLoading()
        binding.run {
            historyHeadline.visibility = View.VISIBLE
            recommendationsHeadline.visibility = View.VISIBLE

            ChipsGroupPopulator(historyChipGroup, R.layout.chip_item_query).run {
                populate(history) { callbacks.onChipClicked(it) }
            }

            ChipsGroupPopulator(suggestionChipGroup, R.layout.chip_item_query).run {
                populate(suggestions) { callbacks.onChipClicked(it) }
            }
        }
    }

    fun showError(messageId: Int = R.string.error_snackbar_cannot_load_suggestions) {
        val message = context.getString(messageId)
        binding.run {
            Snackbar
                .make(searchScreenRoot, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK") { queryTextInput.error = null }
                .show()
        }
    }

    fun showLoading() {
        binding.searchSwipeToRefresh.isRefreshing = true
    }

    private fun hideLoading() {
        binding.searchSwipeToRefresh.isRefreshing = false
    }

    private fun TextInputEditText.checkQueryBeforeProceed(actionId: Int) {
        val actual = editableText.toString()

        if (actionId == EditorInfo.IME_ACTION_DONE) {

            if (SearchQueryValidation.validate(actual)) callbacks.onQuerySubmited(actual)
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
