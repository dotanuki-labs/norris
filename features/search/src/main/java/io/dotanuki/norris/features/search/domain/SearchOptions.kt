package io.dotanuki.norris.features.search.domain

data class SearchOptions(
    val recommendations: List<String>,
    val history: List<String>
)
