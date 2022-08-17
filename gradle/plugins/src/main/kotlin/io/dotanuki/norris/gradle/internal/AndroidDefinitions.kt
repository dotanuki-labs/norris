package io.dotanuki.norris.gradle.internal

import java.util.Collections

internal object AndroidDefinitions {

    const val compileSdk = 32
    const val minSdk = 25
    const val targetSdk = compileSdk

    const val buildToolsVersion = "32.0.0"
    val noGeneratedDensities = Collections.emptySet<String>().toTypedArray()
}
