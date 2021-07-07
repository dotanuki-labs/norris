package io.dotanuki.norris.search.util

import android.view.View
import io.dotanuki.norris.search.presentation.SearchScreenState
import io.dotanuki.norris.search.ui.SearchActivity
import io.dotanuki.norris.search.ui.SearchScreen
import io.dotanuki.testing.app.TestApplication
import org.kodein.di.direct
import org.kodein.di.instance

class FakeSearchScreen : SearchScreen {

    val trackedStates = mutableListOf<SearchScreenState>()
    var isLinked: Boolean = false
    lateinit var delegate: SearchScreen.Delegate

    override fun link(host: SearchActivity, callbacks: SearchScreen.Delegate): View {
        delegate = callbacks
        isLinked = true
        return View(host)
    }

    override fun updateWith(newState: SearchScreenState) {
        trackedStates += newState
    }

    companion object {
        fun TestApplication.searchScreen(): FakeSearchScreen =
            di.direct.instance<SearchScreen>() as FakeSearchScreen
    }
}
