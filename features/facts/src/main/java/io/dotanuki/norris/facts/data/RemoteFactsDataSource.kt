package io.dotanuki.norris.facts.data

import io.dotanuki.norris.facts.domain.ChuckNorrisFact
import io.dotanuki.norris.facts.domain.FactsRetrievalError
import io.dotanuki.norris.rest.ChuckNorrisDotIO
import io.dotanuki.norris.rest.RawSearch
import io.dotanuki.norris.rest.managedExecution

class RemoteFactsDataSource(
    private val api: ChuckNorrisDotIO
) {

    suspend fun search(term: String): List<ChuckNorrisFact> {
        if (term.isEmpty()) throw FactsRetrievalError.NoResultsFound

        return managedExecution {
            api.search(term).asChuckNorrisFacts()
        }
    }

    private fun RawSearch.asChuckNorrisFacts() =
        result.map {
            ChuckNorrisFact(
                id = it.id,
                shareableUrl = it.url,
                textual = it.value,
            )
        }
}
