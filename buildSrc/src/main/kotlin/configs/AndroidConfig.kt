package configs

import java.util.Collections.emptySet

object AndroidConfig {

    const val applicationId = "io.dotanuki.demos.norris"

    const val compileSdk = 29
    const val minSdk = 23
    const val targetSdk = compileSdk

    const val buildToolsVersion = "30.0.1"

    const val instrumentationTestRunner = "androidx.test.runner.AndroidJUnitRunner"

    val noGeneratedDensities = emptySet<String>().toTypedArray()
}
