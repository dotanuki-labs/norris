package io.dotanuki.norris.gradle.modules.conventions

import com.android.build.api.dsl.ApplicationExtension
import io.dotanuki.norris.gradle.modules.models.ProguardRules
import org.gradle.api.Project

internal fun Project.isTestMode(): Boolean = properties["testMode"]?.let { true } ?: false

@Suppress("LongMethod")
internal fun Project.applyAndroidApplicationConventions() {
    applyAndroidStandardConventions()

    val android = extensions.findByName("android") as ApplicationExtension

    android.apply {
        defaultConfig {
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            applicationId = "io.dotanuki.norris.android"
        }

        signingConfigs {
            create("release").initWith(getByName("debug"))
        }

        buildTypes {
            getByName("debug") {
                applicationIdSuffix = ".debug"
                versionNameSuffix = "-DEBUG"
                enableAndroidTestCoverage = false
                enableUnitTestCoverage = false
                buildConfigField("boolean", "IS_TEST_MODE", "${project.isTestMode()}")
            }

            getByName("release") {
                isMinifyEnabled = true
                isShrinkResources = true

                val proguardRules = ProguardRules("$rootDir/app/proguard")
                proguardFiles(*(proguardRules.extras))
                proguardFiles(getDefaultProguardFile(proguardRules.androidDefault))
                signingConfig = signingConfigs.findByName("release")
                buildConfigField("boolean", "IS_TEST_MODE", "${project.isTestMode()}")
            }
        }

        packagingOptions {
            resources.excludes.addAll(
                listOf(
                    "COPYRIGHT.txt"
                )
            )
        }
    }
}
