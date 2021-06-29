package io.dotanuki.demos.norris.navigation

import android.app.Activity
import io.dotanuki.norris.facts.ui.FactsActivity
import io.dotanuki.norris.navigator.Screen
import io.dotanuki.norris.search.ui.SearchActivity

object ScreenLinks {

    val associations by lazy {
        mapOf<Screen, Class<out Activity>>(
            Screen.FactsList to FactsActivity::class.java,
            Screen.SearchQuery to SearchActivity::class.java
        )
    }
}
