package io.dotanuki.platform.android.testing.persistance

import android.app.Application
import androidx.test.platform.app.InstrumentationRegistry
import io.dotanuki.platform.android.core.persistance.LocalStorage
import io.dotanuki.platform.android.core.persistance.PersistanceContextRegistry
import io.dotanuki.platform.android.core.persistance.di.LocalStorageFactory

class StorageTestHelper {

    private var sut: LocalStorage? = null

    fun createStorage(): LocalStorage {
        val instrumentionContext = InstrumentationRegistry.getInstrumentation().targetContext
        val app = instrumentionContext.applicationContext as Application
        PersistanceContextRegistry.register(app)

        return LocalStorageFactory.create().apply { sut = this }
    }

    fun clearStorage() {
        createStorage().destroy()
    }

    fun registerNewSearch(term: String) {
        createStorage().registerNewSearch(term)
    }
}
