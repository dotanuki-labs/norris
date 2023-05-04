package io.dotanuki.platform.android.core.persistance.di

import android.content.Context
import io.dotanuki.platform.android.core.persistance.LocalStorage
import io.dotanuki.platform.android.core.persistance.PersistanceContextRegistry

object LocalStorageFactory {

    private const val prefsName = "last-searches"

    private val memoized by lazy {
        val appContext = PersistanceContextRegistry.targetContext()
        val prefs = appContext.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        LocalStorage(prefs)
    }

    fun create(): LocalStorage = memoized
}
