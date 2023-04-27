package io.dotanuki.platform.jvm.core.rest.util

enum class ToxicityLevel(val value: Float) {
    LOW(0.1f),
    MODERATE(0.3f),
    EXTREME(0.99f)
}
