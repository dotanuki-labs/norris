package io.dotanuki.platform.android.testing.persistance

import android.app.Application
import androidx.test.platform.app.InstrumentationRegistry
import io.dotanuki.platform.android.core.persistance.PersistanceContextRegistry
import io.dotanuki.platform.android.core.persistance.di.LocalStorageFactory

class StorageTestHelper {

    val storage by lazy {
        val instrumentionContext = InstrumentationRegistry.getInstrumentation().targetContext
        val app = instrumentionContext.applicationContext as Application
        PersistanceContextRegistry.register(app)
        LocalStorageFactory.create().also { it.erase() }
    }
}
