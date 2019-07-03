package io.dotanuki.norris.navigator

import android.os.Bundle

object DefineSearchQuery : DelegatableWork {
    override val tag = 0xABCD
    const val KEY_SEARCH_QUERY = "key.search.query"

    fun toPayload(query: String) = Bundle().apply {
        putString(KEY_SEARCH_QUERY, query)
    }

    fun unwrapQuery(payload: Bundle) =
        payload.getString(KEY_SEARCH_QUERY).let { it } ?: throw UNWRAP_ERROR

    private val UNWRAP_ERROR = IllegalArgumentException("Cannot unwrap this payload")
}