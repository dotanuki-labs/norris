package io.dotanuki.norris.gradle.modules.models

internal data class AppVersion(
    val name: String,
    val major: Int,
    val minor: Int,
    val patch: Int,
    val code: Int
)
