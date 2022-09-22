package io.dotanuki.app.navigation

import android.app.Activity
import io.dotanuki.features.facts.ui.FactsActivity
import io.dotanuki.features.search.ui.SearchActivity
import io.dotanuki.platform.android.core.navigator.Screen

object ScreenLinks {

    val associations by lazy {
        mapOf<Screen, Class<out Activity>>(
            Screen.FactsList to FactsActivity::class.java,
            Screen.SearchQuery to SearchActivity::class.java
        )
    }
}
