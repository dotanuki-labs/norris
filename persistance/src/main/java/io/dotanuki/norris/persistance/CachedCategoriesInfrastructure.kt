package io.dotanuki.norris.persistance

import io.dotanuki.norris.domain.model.RelatedCategory
import io.dotanuki.norris.domain.services.CategoriesCacheService

object CachedCategoriesInfrastructure : CategoriesCacheService {

    private var cache: List<RelatedCategory.Available>? = null

    override fun save(categories: List<RelatedCategory.Available>) {
        cache = categories
    }

    override fun cached() = cache
}