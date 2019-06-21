package io.dotanuki.norris.rest.services

import io.dotanuki.norris.rest.model.ChuckNorrisFact
import io.dotanuki.norris.rest.model.RelatedCategory

interface RemoteFactsService {

    suspend fun availableCategories(): List<RelatedCategory.Available>

    suspend fun fetchFacts(searchTerm: String): List<ChuckNorrisFact>
}