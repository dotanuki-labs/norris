package io.dotanuki.platform.jvm.core.rest

import kotlinx.serialization.Serializable

@Serializable
data class RawSearch(
    val result: List<RawFact>
)

@Serializable
data class RawFact(
    val id: String,
    val url: String,
    val value: String,
    val categories: List<String>
)
