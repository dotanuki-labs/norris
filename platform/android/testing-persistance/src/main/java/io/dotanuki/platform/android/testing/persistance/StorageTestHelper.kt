package io.dotanuki.platform.android.testing.persistance

import android.app.Application
import androidx.test.platform.app.InstrumentationRegistry
import io.dotanuki.platform.android.core.persistance.AppPreferencesWrapper
import io.dotanuki.platform.android.core.persistance.LocalStorage

class StorageTestHelper {

    private var sut: LocalStorage? = null

    fun createStorage(): LocalStorage {
        val instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
        val app = instrumentationContext.applicationContext as Application
        return LocalStorage(AppPreferencesWrapper(app).preferences).apply { sut = this }
    }

    fun clearStorage() {
        createStorage().destroy()
    }

    fun registerNewSearch(term: String) {
        createStorage().registerNewSearch(term)
    }
}
