package io.dotanuki.platform.android.core.persistance.di

import android.app.Application
import io.dotanuki.platform.android.core.persistance.AppPreferencesWrapper
import io.dotanuki.platform.android.core.persistance.LocalStorage

object LocalStorageFactory {

    private var memoized: LocalStorage? = null

    fun create(app: Application): LocalStorage =
        memoized ?: newLocalStorate(app).apply { memoized = this }

    private fun newLocalStorate(app: Application) =
        LocalStorage(AppPreferencesWrapper(app).preferences)
}
