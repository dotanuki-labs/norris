package io.dotanuki.norris.rest

import io.dotanuki.norris.domain.model.ChuckNorrisFact
import io.dotanuki.norris.domain.model.RelatedCategory.Available
import io.dotanuki.norris.domain.model.RelatedCategory.Missing

internal fun RawCategories.asRelatedCategories() = raw.map { Available(it) }

internal fun List<String>.asRelevantCategory() =
    if (isEmpty()) Missing else Available(first())

internal fun RawSearch.asChuckNorrisFacts() =
    result.map {
        ChuckNorrisFact(
            id = it.id,
            shareableUrl = it.url,
            textual = it.value,
            category = it.categories.asRelevantCategory()
        )
    }
