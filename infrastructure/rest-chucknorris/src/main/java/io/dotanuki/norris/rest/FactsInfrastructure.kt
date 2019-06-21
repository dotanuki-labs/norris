package io.dotanuki.norris.rest

import io.dotanuki.norris.rest.services.RemoteFactsService

internal class FactsInfrastructure(private val rest: ChuckNorrisDotIO) : RemoteFactsService {

    override suspend fun availableCategories() =
        managedExecution {
            rest.categories().asRelatedCategories()
        }

    override suspend fun fetchFacts(searchTerm: String) =
        managedExecution {
            rest.search(searchTerm).asChuckNorrisFacts()
        }
}