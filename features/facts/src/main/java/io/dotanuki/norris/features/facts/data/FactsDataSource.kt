package io.dotanuki.norris.features.facts.data

import io.dotanuki.norris.features.facts.domain.ChuckNorrisFact
import io.dotanuki.norris.features.facts.domain.FactsRetrievalError
import io.dotanuki.norris.rest.ChuckNorrisDotIO
import io.dotanuki.norris.rest.RawSearch
import io.dotanuki.norris.rest.managedExecution

class FactsDataSource(
    private val api: ChuckNorrisDotIO
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
