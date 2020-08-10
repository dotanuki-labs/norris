package io.dotanuki.demos.norris.fakes

import io.dotanuki.norris.domain.model.RelatedCategory
import io.dotanuki.norris.domain.services.CategoriesCacheService

class FakeCategoriesCache : CategoriesCacheService {
    override fun save(categories: List<RelatedCategory.Available>) = Unit

    override fun cached(): List<RelatedCategory.Available>? = listOf(
        RelatedCategory.Available("dev"),
        RelatedCategory.Available("humor")
    )
}