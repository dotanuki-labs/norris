package io.dotanuki.platform.android.testing.app

import android.app.Activity
import android.app.Application
import io.dotanuki.platform.android.core.navigator.Screen
import io.dotanuki.platform.android.core.navigator.ScreenMappingProvider

class TestApplication : Application(), ScreenMappingProvider {

    override fun screenMap(): Map<Screen, Class<out Activity>> = emptyMap()
}
