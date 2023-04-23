package io.dotanuki.app

import android.app.Activity
import android.app.Application
import io.dotanuki.app.navigation.ScreenLinks
import io.dotanuki.platform.android.core.navigator.Screen
import io.dotanuki.platform.android.core.navigator.ScreenMappingProvider

class NorrisApplication : Application(), ScreenMappingProvider {

    override fun screenMap(): Map<Screen, Class<out Activity>> = ScreenLinks.associations
}
