package io.dotanuki.platform.android.testing.persistence

import android.app.Application
import androidx.test.platform.app.InstrumentationRegistry
import io.dotanuki.platform.android.core.persistence.PersistenceContextRegistry
import io.dotanuki.platform.android.core.persistence.di.LocalStorageFactory

class StorageTestHelper {
    val storage by lazy {
        val instrumentionContext = InstrumentationRegistry.getInstrumentation().targetContext
        val app = instrumentionContext.applicationContext as Application
        PersistenceContextRegistry.register(app)
        LocalStorageFactory.create().also { it.erase() }
    }
}
