package io.dotanuki.features.facts.data

import io.dotanuki.features.facts.domain.ChuckNorrisFact
import io.dotanuki.features.facts.domain.FactsRetrievalError
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisServiceClient
import io.dotanuki.platform.jvm.core.rest.RawSearch

class FactsDataSource(
    private val client: ChuckNorrisServiceClient
) {

    suspend fun search(term: String): List<ChuckNorrisFact> {
        if (term.isEmpty()) throw FactsRetrievalError.EmptyTerm

        return client.search(term).asChuckNorrisFacts()
    }

    private fun RawSearch.asChuckNorrisFacts() =
        result.map {
            ChuckNorrisFact(
                id = it.id,
                shareableUrl = it.url,
                textual = it.value
            )
        }
}
