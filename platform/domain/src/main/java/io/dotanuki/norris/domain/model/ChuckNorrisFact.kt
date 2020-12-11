package io.dotanuki.norris.domain.model

data class ChuckNorrisFact(
    val id: String,
    val category: RelatedCategory,
    val shareableUrl: String,
    val textual: String
)
