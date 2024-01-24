package io.dotanuki.platform.android.core.navigator

import android.app.Activity

interface ScreenMappingProvider {
    fun screenMap(): Map<Screen, Class<out Activity>>
}
