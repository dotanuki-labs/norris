package io.dotanuki.platform.android.core.persistance

import android.app.Application
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalStorageTests {

    private val storage by lazy {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application
        val wrapper = AppPreferencesWrapper(app)
        LocalStorage(wrapper.preferences)
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
