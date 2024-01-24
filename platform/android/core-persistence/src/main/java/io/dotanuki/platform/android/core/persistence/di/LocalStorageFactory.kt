package io.dotanuki.platform.android.core.persistence.di

import android.content.Context
import io.dotanuki.platform.android.core.persistence.LocalStorage
import io.dotanuki.platform.android.core.persistence.PersistenceContextRegistry

object LocalStorageFactory {
    private const val prefsName = "last-searches"

    private val memoized by lazy {
        val appContext = PersistenceContextRegistry.targetContext()
        val prefs = appContext.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        LocalStorage(prefs)
    }

    fun create(): LocalStorage = memoized
}
