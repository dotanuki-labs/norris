package io.dotanuki.platform.android.core.persistance.di

import android.app.Application
import io.dotanuki.platform.android.core.persistance.AppPreferencesWrapper
import io.dotanuki.platform.android.core.persistance.LocalStorage

class PersistanceModule(private val app: Application) {

    val localStorage by lazy {
        LocalStorage(AppPreferencesWrapper(app).preferences)
    }
}
