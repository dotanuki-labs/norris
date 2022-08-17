package io.dotanuki.norris.gradle.internal

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.BuildConfigField
import com.android.build.gradle.BaseExtension
import com.slack.keeper.optInToKeeper
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.util.Collections
import java.util.Properties

internal object AndroidDefinitions {

    const val compileSdk = 32
    const val minSdk = 25
    const val targetSdk = compileSdk

    const val buildToolsVersion = "32.0.0"
    const val instrumentationTestRunner = "androidx.test.runner.AndroidJUnitRunner"
    val noGeneratedDensities = Collections.emptySet<String>().toTypedArray()
}

internal fun Project.isTestMode(): Boolean = properties["testMode"]?.let { true } ?: false

internal fun Project.applyAndroidStandardConventions() {

    val android = extensions.findByName("android") as BaseExtension

    android.run {
        compileSdkVersion(AndroidDefinitions.compileSdk)
        buildToolsVersion(AndroidDefinitions.buildToolsVersion)

        defaultConfig {

            minSdk = AndroidDefinitions.minSdk
            targetSdk = AndroidDefinitions.targetSdk
            versionCode = Versioning.version.code
            versionName = Versioning.version.name

            vectorDrawables.run {
                useSupportLibrary = true
                generatedDensities(*(AndroidDefinitions.noGeneratedDensities))
            }

            resourceConfigurations.add("en")
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        testOptions {
            unitTests.isReturnDefaultValues = true
            unitTests.isIncludeAndroidResources = true
            unitTests.all {
                // https://github.com/robolectric/robolectric/issues/3023
                it.jvmArgs.addAll(listOf("-ea", "-noverify"))
            }
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

internal fun Project.applyAndroidApplicationConventions() {
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

    androidComponents.onVariants {
        val testModeConfig = BuildConfigField("boolean", "IS_TEST_MODE", "${project.isTestMode()}")
        it.buildConfigFields.put("TEST_MODE", testModeConfig)
    }

    androidComponents.finalizeDsl { android ->
        android.apply {

            testBuildType = when {
                isTestMode() -> "release"
                else -> "debug"
            }

            defaultConfig {
                testInstrumentationRunner = AndroidDefinitions.instrumentationTestRunner

                if (isTestMode()) {
                    testInstrumentationRunnerArguments["listener"] = "leakcanary.FailTestOnLeakRunListener"
                }
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
                    isTestCoverageEnabled = false
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
    }
}
