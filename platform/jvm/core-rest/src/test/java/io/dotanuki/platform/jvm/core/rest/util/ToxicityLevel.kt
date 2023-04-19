package io.dotanuki.platform.jvm.core.rest.util

enum class ToxicityLevel(val value: Float) {
    LOW(0.1f),
    MEDIUM(0.3f),
    HIGH(0.7f),
    EXTREME(0.99f)
}
