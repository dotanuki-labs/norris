package io.dotanuki.norris.search.util

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.dotanuki.norris.search.presentation.SearchScreenState
import io.dotanuki.norris.search.ui.SearchScreen
import io.dotanuki.testing.app.TestApplication
import org.kodein.di.direct
import org.kodein.di.instance

class FakeSearchScreen : SearchScreen {

    val trackedStates = mutableListOf<SearchScreenState>()
    var isLinked: Boolean = false
    lateinit var screenDelegate: SearchScreen.Delegate

    override fun link(host: AppCompatActivity, delegate: SearchScreen.Delegate): View {
        screenDelegate = delegate
        isLinked = true
        return View(host)
    }

    override fun updateWith(newState: SearchScreenState) {
        trackedStates += newState
    }

    companion object {
        fun from(app: TestApplication): FakeSearchScreen =
            app.di.direct.instance<SearchScreen>() as FakeSearchScreen
    }
}
