package io.dotanuki.norris.facts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.dotanuki.norris.facts.ui.FactsScreen
import io.dotanuki.norris.facts.ui.WrappedContainer

class FactsTestActivity : AppCompatActivity() {

    val screen by lazy { WrappedContainer() }

    private val testDelegate by lazy {
        object : FactsScreen.Delegate {
            override fun onRefresh() = Unit

            override fun onSearch() = Unit

            override fun onShare(fact: String) = Unit
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = screen.link(this, testDelegate)
        setContentView(root)
    }
}
