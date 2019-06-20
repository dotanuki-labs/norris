package io.dotanuki.norris.domain.services

import io.dotanuki.norris.domain.model.ChuckNorrisFact
import io.dotanuki.norris.domain.model.RelatedCategory

interface RemoteFactsService {

    suspend fun availableCategories(): List<RelatedCategory.Available>

    suspend fun fetchFacts(searchTerm: String): List<ChuckNorrisFact>
}