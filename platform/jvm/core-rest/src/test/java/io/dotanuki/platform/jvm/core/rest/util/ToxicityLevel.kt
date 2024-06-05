package io.dotanuki.platform.jvm.core.rest.util

enum class ToxicityLevel(
    val value: Float,
) {
    LOW(0.01f),
    MODERATE(0.3f),
}
