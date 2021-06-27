package io.dotanuki.norris.persistance

import android.app.Application
import android.content.Context.MODE_PRIVATE

class AppPreferencesWrapper(private val app: Application) {

    val preferences by lazy {
        app.getSharedPreferences(PREFS_FILE, MODE_PRIVATE)
    }

    private companion object {
        const val PREFS_FILE = "last-searches"
    }
}
