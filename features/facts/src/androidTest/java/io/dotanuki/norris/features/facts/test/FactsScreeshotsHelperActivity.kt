package io.dotanuki.norris.features.facts.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.dotanuki.norris.features.facts.ui.FactsEventsHandler
import io.dotanuki.norris.features.facts.ui.FactsView

class FactsScreeshotsHelperActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = FactsView.create(this, FactsEventsHandler.NoOp)
        setContentView(root)
    }
}
