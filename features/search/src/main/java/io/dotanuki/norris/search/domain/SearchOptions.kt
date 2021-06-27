package io.dotanuki.norris.search.domain

data class SearchOptions(
    val recommendations: List<String>,
    val history: List<String>
)
