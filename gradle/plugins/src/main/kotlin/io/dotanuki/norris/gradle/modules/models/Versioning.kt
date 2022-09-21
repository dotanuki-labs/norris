package io.dotanuki.norris.gradle.modules.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal object Versioning {
    private const val major = 2
    private const val minor = 0
    private const val patch = 0
    private const val code = 100 * major + 10 * minor + patch

    private val timestamp by lazy {
        System.getenv(IS_DISTRIBUTION_BUILD)?.let { computeDateTimestamp() } ?: "SNAPSHOT"
    }

    private val name by lazy {
        "$major.$minor.$patch-$timestamp"
    }

    @JvmStatic val version by lazy {
        AppVersion(name, major, minor, patch, code)
    }

    private fun computeDateTimestamp() =
        SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())

    private const val IS_DISTRIBUTION_BUILD = "NORRIS_DISTRIBUTION_BUILD"
}
