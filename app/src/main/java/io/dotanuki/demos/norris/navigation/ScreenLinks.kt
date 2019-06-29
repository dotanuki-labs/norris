package io.dotanuki.demos.norris.navigation

import android.app.Activity
import io.dotanuki.norris.facts.FactsActivity
import io.dotanuki.norris.features.navigator.Screen
import io.dotanuki.norris.search.SearchQueryActivity

object ScreenLinks {

    val associations by lazy {
        mapOf<Screen, Class<out Activity>>(
            Screen.FactsList to FactsActivity::class.java,
            Screen.SearchQuery to SearchQueryActivity::class.java
        )
    }
}