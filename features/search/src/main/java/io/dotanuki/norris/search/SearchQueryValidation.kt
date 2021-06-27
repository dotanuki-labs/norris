package io.dotanuki.norris.search

object SearchQueryValidation {

    fun validate(query: String) = REGEX.matches(query)

    private val REGEX by lazy {
        "^[a-zA-Z]{3,10}\$".toRegex()
    }
}
