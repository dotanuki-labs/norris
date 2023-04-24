package io.dotanuki.platform.android.testing.persistance

import android.app.Application
import androidx.test.platform.app.InstrumentationRegistry
import io.dotanuki.platform.android.core.persistance.AppPreferencesWrapper
import io.dotanuki.platform.android.core.persistance.LocalStorage
import kotlinx.coroutines.runBlocking

object PersistanceHelper {

    val storage: LocalStorage by lazy {
        val instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
        val app = instrumentationContext.applicationContext as Application
        LocalStorage(AppPreferencesWrapper(app).preferences)
    }

    fun clearStorage() {
        storage.destroy()
    }

    fun registerNewSearch(term: String) {
        storage.registerNewSearch(term)
    }

    fun savedSearches(): List<String> = runBlocking { storage.lastSearches() }
}
