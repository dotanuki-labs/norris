package io.dotanuki.gradle.shapers.definitions

import java.util.Collections

object AndroidDefinitions {
    const val compileSdk = 31
    const val minSdk = 25
    const val targetSdk = compileSdk

    const val buildToolsVersion = "31.0.0"

    const val instrumentationTestRunner = "androidx.test.runner.AndroidJUnitRunner"

    val noGeneratedDensities = Collections.emptySet<String>().toTypedArray()
}
