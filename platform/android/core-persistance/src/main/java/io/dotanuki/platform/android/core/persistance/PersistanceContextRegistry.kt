package io.dotanuki.platform.android.core.persistance

import android.app.Application

object PersistanceContextRegistry {

    private var app: Application? = null

    fun register(target: Application) {
        app = target
    }

    fun targetContext() = app ?: error("No Context available in this registry")
}
