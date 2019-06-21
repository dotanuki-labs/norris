package io.dotanuki.norris.rest.model

data class ChuckNorrisFact(
    val id: String,
    val category: RelatedCategory,
    val shareableUrl: String,
    val textual: String
)