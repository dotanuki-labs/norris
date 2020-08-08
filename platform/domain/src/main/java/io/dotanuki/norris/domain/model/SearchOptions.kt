package io.dotanuki.norris.domain.model

data class SearchOptions(
    val recommendations: List<String>,
    val history: List<String>
)