package io.dotanuki.platform.android.core.persistance.di

import android.content.Context
import io.dotanuki.platform.android.core.persistance.LocalStorage
import io.dotanuki.platform.android.core.persistance.PersistanceContextRegistry

object LocalStorageFactory {

    private const val PREFS_FILE = "last-searches"

    private var memoized: LocalStorage? = null

    fun create(): LocalStorage =
        memoized ?: newLocalStorate().apply { memoized = this }

    private fun newLocalStorate(): LocalStorage {
        val target = PersistanceContextRegistry.targetContext()
        val prefs = target.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
        return LocalStorage(prefs)
    }
}
