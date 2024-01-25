package io.dotanuki.platform.android.core.persistence

import android.app.Application

object PersistenceContextRegistry {
    private var app: Application? = null

    fun register(target: Application) {
        app = target
    }

    fun targetContext() = app ?: error("No Context available in this registry")
}
