package io.dotanuki.features.facts.data

import io.dotanuki.features.facts.domain.ChuckNorrisFact
import io.dotanuki.features.facts.domain.FactsRetrievalError
import io.dotanuki.platform.jvm.core.rest.ChuckNorrisService
import io.dotanuki.platform.jvm.core.rest.RawSearch
import io.dotanuki.platform.jvm.core.networking.managedExecution

class FactsDataSource(
    private val api: ChuckNorrisService
) {

    suspend fun search(term: String): List<ChuckNorrisFact> {
        if (term.isEmpty()) throw FactsRetrievalError.EmptyTerm

        return managedExecution {
            api.search(term).asChuckNorrisFacts()
        }
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
