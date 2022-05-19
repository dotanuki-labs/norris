package io.dotanuki.gradle.shapers.conventions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.BaseExtension
import com.slack.keeper.optInToKeeper
import io.dotanuki.gradle.catalogsourcer.DependabotBridge
import io.dotanuki.gradle.shapers.definitions.AndroidDefinitions
import io.dotanuki.gradle.shapers.definitions.ProguardDefinitions
import io.dotanuki.gradle.shapers.definitions.Versioning
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.util.*

fun Project.applyAndroidStandardConventions() {
    val android = extensions.findByName("android") as BaseExtension

    android.apply {
        compileSdkVersion(AndroidDefinitions.compileSdk)
        buildToolsVersion(AndroidDefinitions.buildToolsVersion)

        defaultConfig {

            it.minSdk = AndroidDefinitions.minSdk
            it.targetSdk = AndroidDefinitions.targetSdk
            it.versionCode = Versioning.version.code
            it.versionName = Versioning.version.name

            it.vectorDrawables.apply {
                useSupportLibrary = true
                generatedDensities(*(AndroidDefinitions.noGeneratedDensities))
            }

            it.resourceConfigurations.add("en")
        }

        compileOptions {
            it.sourceCompatibility = JavaVersion.VERSION_11
            it.targetCompatibility = JavaVersion.VERSION_11
        }

        testOptions {
            it.unitTests.isReturnDefaultValues = true
            it.unitTests.isIncludeAndroidResources = true
            it.unitTests.all { unitTests ->
                // https://github.com/robolectric/robolectric/issues/3023
                unitTests.jvmArgs?.addAll(
                    listOf("-ea", "-noverify")
                )
            }
        }
    }
}

fun Project.applyAndroidLibraryConventions() {
    applyAndroidStandardConventions()

    val android = extensions.findByName("android") as BaseExtension

    android.apply {
        buildTypes {
            it.getByName("release") { buildType ->
                buildType.isMinifyEnabled = true

                val proguardDefinitions = ProguardDefinitions("$rootDir/app/proguard")
                buildType.proguardFiles(*(proguardDefinitions.customRules))
                buildType.proguardFiles(getDefaultProguardFile(proguardDefinitions.androidRules))
            }
        }
    }

    tasks.whenTaskAdded {
        if (name.startsWith("test") and name.contains("DebugUnitTest")) {
            it.enabled = false
        }
    }
}

fun Project.applyAndroidApplicationConventions() {
    applyAndroidStandardConventions()

    val android = extensions.findByName("android") as ApplicationExtension
    val androidComponents = extensions.findByName("androidComponents") as ApplicationAndroidComponentsExtension

    androidComponents.beforeVariants { builder ->
        if (isTestMode()) {
            builder.optInToKeeper() // Helpful extension function
        }
    }

//    val keeper = extensions.findByName("keeper") as KeeperExtension
//    keeper.variantFilter {
//        it.setIgnore(name != "release")
//    }

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
            create("release") { signingConfig ->
                val signingProperties = Properties().apply {
                    load(FileInputStream("$rootDir/signing.properties"))
                }

                signingProperties.run {
                    signingConfig.storeFile = File("$rootDir/dotanuki-demos.jks")
                    signingConfig.storePassword = getProperty("io.dotanuki.norris.storepass")
                    signingConfig.keyAlias = getProperty("io.dotanuki.norris.keyalias")
                    signingConfig.keyPassword = getProperty("io.dotanuki.norris.keypass")
                }
            }
        }

        buildTypes {

            fun Project.configureHttps(buildType: BuildType) {

                val apiUrl = when {
                    isTestMode() -> "http://localhost:4242"
                    else -> "https://api.chucknorris.io"
                }

                buildType.run {
                    buildConfigField("String", "CHUCKNORRIS_API_URL", "\"${apiUrl}\"")
                    resValue("bool", "clear_networking_traffic_enabled", "${project.isTestMode()}")
                }
            }

            getByName("debug") { buildType ->
                buildType.applicationIdSuffix = ".debug"
                buildType.versionNameSuffix = "-DEBUG"
                buildType.isTestCoverageEnabled = true
                configureHttps(buildType)
            }

            getByName("release") { buildType ->
                buildType.isMinifyEnabled = true
                buildType.isShrinkResources = true

                val proguardDefinitions = ProguardDefinitions("$rootDir/app/proguard")
                buildType.proguardFiles(*(proguardDefinitions.customRules))
                buildType.proguardFiles(getDefaultProguardFile(proguardDefinitions.androidRules))
                buildType.signingConfig = signingConfigs.findByName("release")
                configureHttps(buildType)
            }
        }

        packagingOptions {
            jniLibs.useLegacyPackaging = true
        }
    }

    if (isTestMode()) {
        dependencies.add("releaseImplementation", DependabotBridge.extractDependencies()["leak-canary-release"])
    }
}
