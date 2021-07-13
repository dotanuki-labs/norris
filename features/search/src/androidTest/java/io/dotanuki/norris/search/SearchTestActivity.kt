package io.dotanuki.norris.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.dotanuki.norris.search.ui.SearchScreen
import io.dotanuki.norris.search.ui.WrappedContainer

class SearchTestActivity : AppCompatActivity() {

    val screen by lazy { WrappedContainer() }

    private val testDelegate by lazy {
        object : SearchScreen.Delegate {
            override fun onChipClicked(term: String) = Unit

            override fun onNewSearch(term: String) = Unit
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = screen.link(this, testDelegate)
        setContentView(root)
    }
}
