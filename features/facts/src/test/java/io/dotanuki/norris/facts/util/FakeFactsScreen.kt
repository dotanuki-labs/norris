package io.dotanuki.norris.facts.util

import android.view.View
import io.dotanuki.norris.facts.presentation.FactsScreenState
import io.dotanuki.norris.facts.ui.FactsActivity
import io.dotanuki.norris.facts.ui.FactsScreen
import io.dotanuki.testing.app.TestApplication
import org.kodein.di.direct
import org.kodein.di.instance

class FakeFactsScreen : FactsScreen {

    val trackedStates = mutableListOf<FactsScreenState>()
    var isLinked: Boolean = false

    override fun link(host: FactsActivity, delegate: FactsScreen.Delegate): View {
        isLinked = true
        return View(host)
    }

    override fun updateWith(newState: FactsScreenState) {
        trackedStates += newState
    }

    companion object {
        fun TestApplication.factsScreen(): FakeFactsScreen =
            di.direct.instance<FactsScreen>() as FakeFactsScreen
    }
}
