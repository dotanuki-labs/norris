package io.dotanuki.norris.gradle.modules.conventions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.BaseExtension
import io.dotanuki.norris.gradle.modules.models.PlatformDefinitions
import io.dotanuki.norris.gradle.modules.models.ProguardRules
import io.dotanuki.norris.gradle.modules.models.Versioning
import org.gradle.api.Project
import wtf.emulator.EwExtension
import java.util.Collections

internal fun Project.isTestMode(): Boolean = properties["testMode"]?.let { true } ?: false

internal fun Project.conventionNamespace(): String {
    val rootDir = rootProject.rootDir.path
    val relativePath = projectDir.path.replace(rootDir, "")
    val relativeNamespace = relativePath.split("/").joinToString(separator = ".") { it.replace("-", ".") }
    return "io.dotanuki$relativeNamespace"
}

internal fun Project.applyAndroidStandardConventions() {

    val android = extensions.findByName("android") as BaseExtension
    val platformDefinitions = PlatformDefinitions.from(this)

    android.run {
        compileSdkVersion(platformDefinitions.androidTargetSdk)
        buildToolsVersion(platformDefinitions.androidBuildToolsVersion)

        namespace = conventionNamespace()

        defaultConfig {
            minSdk = platformDefinitions.androidMinSdk
            targetSdk = platformDefinitions.androidTargetSdk
            versionCode = Versioning.version.code
            versionName = Versioning.version.name

            vectorDrawables.run {
                useSupportLibrary = true
                generatedDensities(*Collections.emptySet<String>().toTypedArray())
            }

            resourceConfigurations.add("en")
        }

        compileOptions {
            sourceCompatibility = platformDefinitions.javaCompatibilityVersion
            targetCompatibility = platformDefinitions.javaCompatibilityVersion
        }

        testOptions {
            unitTests.isReturnDefaultValues = true
            unitTests.isIncludeAndroidResources = true
        }
    }
}

internal fun Project.applyAndroidPlatformLibraryConventions() {

    applyAndroidStandardConventions()

    val android = extensions.findByName("android") as BaseExtension

    android.apply {
        buildTypes {
            getByName("release") {
                isMinifyEnabled = true

                val proguardDefinitions = ProguardRules("$rootDir/app/proguard")
                proguardFiles(*(proguardDefinitions.extras))
                proguardFiles(getDefaultProguardFile(proguardDefinitions.androidDefault))
            }
        }
    }
}

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
                    "META-INF/LGPL2.1"
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
    }
}
