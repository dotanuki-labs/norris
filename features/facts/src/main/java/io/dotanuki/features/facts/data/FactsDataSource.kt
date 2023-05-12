package io.dotanuki.features.facts.data

import io.dotanuki.features.facts.di.FactsContext
import io.dotanuki.features.facts.domain.ChuckNorrisFact
import io.dotanuki.features.facts.domain.FactsRetrievalError
import io.dotanuki.platform.jvm.core.rest.RawSearch

context (FactsContext)
class FactsDataSource {

    suspend fun search(term: String): List<ChuckNorrisFact> {
        if (term.isEmpty()) throw FactsRetrievalError.EmptyTerm

        return restClient.search(term).asChuckNorrisFacts()
    }

    suspend fun actualQuery(): String =
        with(localStorage.lastSearches()) {
            if (isEmpty()) FALLBACK else last()
        }

    private fun RawSearch.asChuckNorrisFacts() =
        result.map {
            ChuckNorrisFact(
                id = it.id,
                shareableUrl = it.url,
                textual = it.value
            )
        }

    companion object {
        const val FALLBACK = ""
    }
}
