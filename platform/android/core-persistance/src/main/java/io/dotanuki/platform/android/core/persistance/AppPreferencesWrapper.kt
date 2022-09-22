package io.dotanuki.platform.android.core.persistance

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class AppPreferencesWrapper(private val app: Application) {

    val preferences: SharedPreferences by lazy {
        app.getSharedPreferences(PREFS_FILE, MODE_PRIVATE)
    }

    private companion object {
        const val PREFS_FILE = "last-searches"
    }
}
