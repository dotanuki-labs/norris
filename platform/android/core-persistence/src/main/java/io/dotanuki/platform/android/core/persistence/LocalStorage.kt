package io.dotanuki.platform.android.core.persistence

import android.content.SharedPreferences
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocalStorage internal constructor(
    private val prefs: SharedPreferences
) {

    suspend fun lastSearches(): List<String> {
        return suspendCoroutine { continuation ->
            continuation.resume(
                retrieveFromPrefs()
            )
        }
    }

    fun registerNewSearch(term: String) {
        val updated = retrieveFromPrefs().filterNot { it == term } + term
        prefs.edit()
            .putString(KEY_TERMS, updated.joinToString(separator = TERMS_SEPARATOR))
            .commit()
    }

    fun erase() {
        prefs.edit().clear().commit()
    }

    private fun retrieveFromPrefs(): List<String> =
        prefs.getString(KEY_TERMS, NO_TERMS)?.let {
            when {
                it.isEmpty() -> emptyList()
                else -> it.split(TERMS_SEPARATOR)
            }
        } ?: emptyList()

    private companion object {
        const val NO_TERMS = ""
        const val TERMS_SEPARATOR = "*+##+*"
        const val KEY_TERMS = "key.search.terms"
    }
}
