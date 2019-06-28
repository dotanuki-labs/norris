package io.dotanuki.norris.persistance

import android.content.SharedPreferences
import io.dotanuki.norris.domain.errors.SearchHistoryError
import io.dotanuki.norris.domain.services.SearchesHistoryService
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class SearchHistoryInfrastructure(
    private val prefs: SharedPreferences
) : SearchesHistoryService {

    override suspend fun lastSearches(): List<String> =
        suspendCoroutine { continuation ->
            continuation.resume(
                retrieveFromPrefs().toList()
            )
        }

    override suspend fun registerNewSearch(term: String) {
        val updated = retrieveFromPrefs() + term
        prefs.edit()
            .putStringSet(KEY_TERMS, updated)
            .commit()
    }

    private fun retrieveFromPrefs() =
        prefs.getStringSet(KEY_TERMS, emptySet())
            ?.let { it }
            ?: throw SearchHistoryError

    private companion object {
        const val KEY_TERMS = "key.search.terms"
    }
}