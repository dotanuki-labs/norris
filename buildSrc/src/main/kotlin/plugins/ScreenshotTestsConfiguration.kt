package plugins

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

fun Project.enableScreenshotTests() {
    apply(plugin = "shot")

    val android = extensions.findByName("android") as BaseExtension

    android.apply {
        defaultConfig {
            testApplicationId = "io.dotanuki.demos.norris.test"
            testInstrumentationRunner = "com.karumi.shot.ShotTestRunner"
        }

        packagingOptions {
            exclude("META-INF/*.kotlin_module")
            exclude("META-INF/AL2.0")
            exclude("META-INF/LGPL2.1")
        }

        testOptions {
            animationsDisabled = true
        }
    }
}
