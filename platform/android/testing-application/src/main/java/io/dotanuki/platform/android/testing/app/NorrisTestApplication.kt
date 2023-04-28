package io.dotanuki.platform.android.testing.app

import android.app.Application
import io.dotanuki.platform.android.core.navigator.Screen
import io.dotanuki.platform.android.core.navigator.ScreenMappingProvider
import io.dotanuki.platform.android.core.persistance.PersistanceContextRegistry

class NorrisTestApplication : Application(), ScreenMappingProvider {

    override fun onCreate() {
        PersistanceContextRegistry.register(this)
        super.onCreate()
    }

    override fun screenMap() = mapOf(
        Screen.FactsList to TestNavigationActivity::class.java,
        Screen.SearchQuery to TestNavigationActivity::class.java
    )
}
