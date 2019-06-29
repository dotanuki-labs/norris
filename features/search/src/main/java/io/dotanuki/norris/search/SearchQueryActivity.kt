package io.dotanuki.norris.search

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import io.dotanuki.norris.features.utilties.toast
import kotlinx.android.synthetic.main.activity_search_query.*

class SearchQueryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_query)
        setup()
    }

    private fun setup() {
        val suggestions = listOf(
            "animal", "career", "celebrity",
            "dev", "explicit", "fashion",
            "food", "history", "money",
            "movie", "music", "political",
            "religion", "science", "sport", "travel"
        )

        ChipsGroupPopulator(suggestionChipGroup, R.layout.chip_item_query).run {
            populate(suggestions) {
                searchForQuery(it)
            }
        }

        val history = listOf(
            "math", "dev", "obama", "trump"
        )

        ChipsGroupPopulator(historyChipGroup, R.layout.chip_item_query).run {
            populate(history) {
                searchForQuery(it)
            }
        }

        queryTextInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchForQuery(
                    queryTextInput.editableText.toString()
                )
            }
            return@setOnEditorActionListener false
        }
    }

    private fun searchForQuery(query: String) {
        toast(query)
    }
}