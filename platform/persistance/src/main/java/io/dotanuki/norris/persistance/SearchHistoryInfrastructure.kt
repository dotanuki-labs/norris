package io.dotanuki.norris.persistance

import android.content.SharedPreferences
import io.dotanuki.norris.domain.services.SearchesHistoryService
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class SearchHistoryInfrastructure(
    private val prefs: SharedPreferences
) : SearchesHistoryService {

    override suspend fun lastSearches(): List<String> {
        return suspendCoroutine { continuation ->
            continuation.resume(
                retrieveFromPrefs()
            )
        }
    }

    override fun registerNewSearch(term: String) {
        val updated = retrieveFromPrefs().filterNot { it == term } + term
        prefs.edit()
            .putString(KEY_TERMS, updated.joinToString(separator = TERMS_SEPARATOR))
            .commit()
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
