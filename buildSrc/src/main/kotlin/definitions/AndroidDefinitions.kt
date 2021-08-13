package definitions

import java.util.Collections

object AndroidDefinitions {

    const val applicationId = "io.dotanuki.demos.norris"

    const val compileSdk = 30
    const val minSdk = 25
    const val targetSdk = compileSdk

    const val buildToolsVersion = "30.0.2"

    const val instrumentationTestRunner = "androidx.test.runner.AndroidJUnitRunner"

    val noGeneratedDensities = Collections.emptySet<String>().toTypedArray()
}
