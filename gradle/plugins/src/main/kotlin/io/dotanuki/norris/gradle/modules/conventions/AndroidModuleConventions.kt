package io.dotanuki.norris.gradle.modules.conventions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.BaseExtension
import com.slack.keeper.optInToKeeper
import com.spotify.ruler.plugin.RulerExtension
import io.dotanuki.norris.gradle.modules.models.PlatformDefinitions
import io.dotanuki.norris.gradle.modules.models.ProguardRules
import io.dotanuki.norris.gradle.modules.models.Versioning
import java.io.File
import java.io.FileInputStream
import java.util.Collections
import java.util.Properties
import org.gradle.api.Project

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
}

@Suppress("LongMethod") internal fun Project.applyAndroidApplicationConventions() {
    applyAndroidStandardConventions()

    if (isTestMode()) {
        pluginManager.apply("com.slack.keeper")
    }

    val androidComponents = extensions.findByName("androidComponents") as ApplicationAndroidComponentsExtension

    androidComponents.beforeVariants {
        if (isTestMode()) {
            it.optInToKeeper()
        }
    }

    val android = extensions.findByName("android") as ApplicationExtension

    android.apply {
        defaultConfig {
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            applicationId = "io.dotanuki.norris.android"
        }

        signingConfigs {
            create("release") {
                val signingProperties = Properties().apply {
                    load(FileInputStream("${rootProject.rootDir}/signing.properties"))
                }

                signingProperties.run {
                    storeFile = File("$rootDir/dotanuki-demos.jks")
                    storePassword = getProperty("io.dotanuki.norris.storepass")
                    keyAlias = getProperty("io.dotanuki.norris.keyalias")
                    keyPassword = getProperty("io.dotanuki.norris.keypass")
                }
            }
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
            jniLibs.useLegacyPackaging = true
        }
    }

    val platformDefinitions = PlatformDefinitions.from(this)
    val ruler = extensions.findByName("ruler") as RulerExtension

    ruler.run {
        abi.set("arm64-v8a")
        locale.set("en")
        screenDensity.set(480)
        sdkVersion.set(platformDefinitions.androidMinSdk)
    }
}
