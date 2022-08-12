package conventions

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project

fun Project.applyScreenshotTestsConventions() {

    val android = extensions.findByName("android") as BaseExtension

    android.apply {
        defaultConfig {
            testApplicationId = "io.dotanuki.demos.norris.test"
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        packagingOptions {
            excludes.add("META-INF/*.kotlin_module")
            excludes.add("META-INF/AL2.0")
            excludes.add("META-INF/LGPL2.1")
        }

        testOptions {
            animationsDisabled = true
        }
    }
}
