package io.dotanuki.platform.android.core.navigator.di

import android.app.Activity
import io.dotanuki.platform.android.core.navigator.Navigator
import io.dotanuki.platform.android.core.navigator.Screen

class NavigatorModule(
    private val links: Map<Screen, Class<out Activity>>
) {
    val navigator by lazy {
        Navigator(links)
    }
}
