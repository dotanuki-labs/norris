package io.dotanuki.norris.domain

import io.dotanuki.norris.domain.model.RelatedCategory
import io.dotanuki.norris.domain.services.CategoriesCacheService
import io.dotanuki.norris.domain.services.RemoteFactsService

class FetchCategories(
    private val categoriesCache: CategoriesCacheService,
    private val remoteFacts: RemoteFactsService
) {

    suspend fun execute(): List<RelatedCategory.Available> =
        with(categoriesCache) {
            cached()
                ?.let { it }
                ?: remoteFacts
                    .availableCategories()
                    .apply { save(this) }
        }
}
