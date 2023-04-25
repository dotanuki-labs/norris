package io.dotanuki.app

import android.app.Application
import io.dotanuki.features.facts.ui.FactsActivity
import io.dotanuki.features.search.ui.SearchActivity
import io.dotanuki.platform.android.core.navigator.Screen
import io.dotanuki.platform.android.core.navigator.ScreenMappingProvider

class NorrisApplication : Application(), ScreenMappingProvider {

    override fun screenMap() = mapOf(
        Screen.FactsList to FactsActivity::class.java,
        Screen.SearchQuery to SearchActivity::class.java
    )
}
