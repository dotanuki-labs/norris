package io.dotanuki.features.search.util

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.dotanuki.features.search.ui.SearchEventsHandler
import io.dotanuki.features.search.ui.SearchView

class SearchScreenshotsHelperActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(SearchView.create(this, SearchEventsHandler.NoOp))
    }
}
