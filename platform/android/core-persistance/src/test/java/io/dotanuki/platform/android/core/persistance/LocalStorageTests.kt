package io.dotanuki.platform.android.core.persistance

import android.app.Application
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import io.dotanuki.platform.android.core.persistance.di.LocalStorageFactory
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalStorageTests {

    private val storage by lazy {
        val instrumentionContext = InstrumentationRegistry.getInstrumentation().targetContext
        val app = instrumentionContext.applicationContext as Application
        PersistanceContextRegistry.register(app)
        LocalStorageFactory.create().also { it.erase() }
    }

    @Test fun `should retrive empty search history`() {
        runBlocking {
            val noHistory = emptyList<String>()
            assertThat(storage.lastSearches()).isEqualTo(noHistory)
        }
    }

    @Test fun `should store new search term`() {
        runBlocking {
            storage.registerNewSearch("Norris")
            val newHistory = listOf("Norris")
            assertThat(storage.lastSearches()).isEqualTo(newHistory)
        }
    }

    @Test fun `should not persist the same term more than once`() {
        runBlocking {
            repeat(times = 3) { storage.registerNewSearch("Norris") }
            val newHistory = listOf("Norris")
            assertThat(storage.lastSearches()).isEqualTo(newHistory)
        }
    }
}
