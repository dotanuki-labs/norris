package definitions

import java.util.Collections

object AndroidDefinitions {

    const val applicationId = "io.dotanuki.demos.norris"

    const val compileSdk = 32
    const val minSdk = 25
    const val targetSdk = compileSdk

    const val buildToolsVersion = "32.0.0"

    const val instrumentationTestRunner = "androidx.test.runner.AndroidJUnitRunner"

    val noGeneratedDensities = Collections.emptySet<String>().toTypedArray()
}
