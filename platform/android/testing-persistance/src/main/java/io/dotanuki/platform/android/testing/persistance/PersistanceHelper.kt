package io.dotanuki.platform.android.testing.persistance

import io.dotanuki.platform.android.core.persistance.LocalStorage
import kotlinx.coroutines.runBlocking

object PersistanceHelper {

    fun clearStorage() {
        retrieveStorage().destroy()
    }

    fun registerNewSearch(term: String) {
        retrieveStorage().registerNewSearch(term)
    }

    fun savedSearches(): List<String> = runBlocking { retrieveStorage().lastSearches() }

    private fun retrieveStorage(): LocalStorage {
//        val app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
//        return (app as DIAware).di.direct.instance()
        TODO()
    }
}
