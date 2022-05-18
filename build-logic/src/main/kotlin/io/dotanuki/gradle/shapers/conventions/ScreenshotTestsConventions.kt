package io.dotanuki.gradle.shapers.conventions

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project

fun Project.applyScreenshotTestsConventions() {

    val android = extensions.findByName("android") as BaseExtension

    android.apply {
        defaultConfig {
            it.testApplicationId = "io.dotanuki.demos.norris.test"
            it.testInstrumentationRunner = "com.karumi.shot.ShotTestRunner"
        }


        packagingOptions {
            it.excludes.add("META-INF/*.kotlin_module")
            it.excludes.add("META-INF/AL2.0")
            it.excludes.add("META-INF/LGPL2.1")
        }

        testOptions {
            it.animationsDisabled = true
        }
    }
}
