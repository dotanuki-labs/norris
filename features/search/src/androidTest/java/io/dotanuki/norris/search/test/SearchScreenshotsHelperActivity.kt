package io.dotanuki.norris.search.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.dotanuki.norris.search.ui.SearchEventsHandler
import io.dotanuki.norris.search.ui.SearchView

class SearchScreenshotsHelperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(SearchView.create(this, SearchEventsHandler.NoOp))
    }
}
