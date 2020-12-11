package io.dotanuki.norris.domain.services

import io.dotanuki.norris.domain.model.RelatedCategory

interface CategoriesCacheService {

    fun save(categories: List<RelatedCategory.Available>)

    fun cached(): List<RelatedCategory.Available>?
}
