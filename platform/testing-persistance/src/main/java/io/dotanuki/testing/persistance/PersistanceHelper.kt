package io.dotanuki.testing.persistance

import androidx.test.platform.app.InstrumentationRegistry
import io.dotanuki.norris.persistance.LocalStorage
import kotlinx.coroutines.runBlocking
import org.kodein.di.DIAware
import org.kodein.di.direct
import org.kodein.di.instance

object PersistanceHelper {

    fun clearStorage() {
        retrieveStorage().destroy()
    }

    fun registerNewSearch(term: String) {
        retrieveStorage().registerNewSearch(term)
    }

    fun savedSearches(): List<String> = runBlocking { retrieveStorage().lastSearches() }

    private fun retrieveStorage(): LocalStorage {
        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        return (app as DIAware).di.direct.instance()
    }
}
