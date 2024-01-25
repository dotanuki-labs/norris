package io.dotanuki.norris.gradle.modules.conventions

import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import wtf.emulator.EwExtension

@Suppress("LongMethod")
fun Project.applyAndroidFeatureLibraryConventions() {

    applyAndroidPlatformLibraryConventions()

    val android = extensions.findByName("android") as BaseExtension

    android.apply {

        defaultConfig {
            testApplicationId = "io.dotanuki.norris.android.test"
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        packagingOptions {
            resources.excludes.addAll(
                listOf(
                    "META-INF/*.kotlin_module",
                    "META-INF/AL2.0",
                    "META-INF/LGPL2.1",
                    "COPYRIGHT.txt"
                )
            )
        }

        testOptions {
            animationsDisabled = true
        }
    }

    val emulatorWtf = extensions.findByName("emulatorwtf") as EwExtension

    emulatorWtf.run {
        token.set(providers.environmentVariable("EMULATOR_WTF_TOKEN"))
        clearPackageData.set(true)
        useOrchestrator.set(true)
        devices.set(
            listOf(
                mapOf("model" to "Pixel2", "version" to 31)
            )
        )
    }
}
