package io.dotanuki.norris.rest

import io.dotanuki.norris.domain.errors.SearchFactsError
import io.dotanuki.norris.domain.model.ChuckNorrisFact
import io.dotanuki.norris.domain.services.RemoteFactsService

class FactsInfrastructure(private val rest: ChuckNorrisDotIO) : RemoteFactsService {

    override suspend fun availableCategories() =
        managedExecution {
            rest.categories().asRelatedCategories()
        }

    override suspend fun fetchFacts(searchTerm: String): List<ChuckNorrisFact> {

        if (searchTerm.isEmpty()) throw SearchFactsError.EmptyTerm

        return managedExecution {
            rest.search(searchTerm).asChuckNorrisFacts()
        }
    }
}
