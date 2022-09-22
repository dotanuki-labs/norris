package io.dotanuki.features.facts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.dotanuki.features.facts.ui.FactsEventsHandler
import io.dotanuki.features.facts.ui.FactsView

class FactsScreeshotsHelperActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = FactsView.create(this, FactsEventsHandler.NoOp)
        setContentView(root)
    }
}
