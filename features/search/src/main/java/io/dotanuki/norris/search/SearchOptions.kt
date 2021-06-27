package io.dotanuki.norris.search

data class SearchOptions(
    val recommendations: List<String>,
    val history: List<String>
)
