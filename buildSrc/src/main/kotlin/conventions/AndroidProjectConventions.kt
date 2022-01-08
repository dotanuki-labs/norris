package conventions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.gradle.BaseExtension
import com.slack.keeper.KeeperExtension
import definitions.AndroidDefinitions
import definitions.ProguardDefinitions
import definitions.Versioning
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

            minSdk = AndroidDefinitions.minSdk
            targetSdk = AndroidDefinitions.targetSdk
            versionCode = Versioning.version.code
            versionName = Versioning.version.name

            vectorDrawables.apply {
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
                it.jvmArgs?.addAll(
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
            getByName("release") {
                isMinifyEnabled = true

                val proguardDefinitions = ProguardDefinitions("$rootDir/app/proguard")
                proguardFiles(*(proguardDefinitions.customRules))
                proguardFiles(getDefaultProguardFile(proguardDefinitions.androidRules))
            }
        }
    }

    tasks.whenTaskAdded {
        if (name.startsWith("test") and name.contains("DebugUnitTest")) {
            enabled = false
        }
    }
}

fun Project.applyAndroidApplicationConventions() {
    applyAndroidStandardConventions()

    val android = extensions.findByName("android") as ApplicationExtension

    if (isTestMode()) {
        val keeper = extensions.findByName("keeper") as KeeperExtension
        keeper.variantFilter {
            setIgnore(name != "release")
        }
    }

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
                    load(FileInputStream("$rootDir/signing.properties"))
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

            getByName("debug") {
                applicationIdSuffix = ".debug"
                versionNameSuffix = "-DEBUG"
                isTestCoverageEnabled = true
                configureHttps(this)
            }

            getByName("release") {
                isMinifyEnabled = true
                isShrinkResources = true

                val proguardDefinitions = ProguardDefinitions("$rootDir/app/proguard")
                proguardFiles(*(proguardDefinitions.customRules))
                proguardFiles(getDefaultProguardFile(proguardDefinitions.androidRules))
                signingConfig = signingConfigs.findByName("release")
                configureHttps(this)
            }
        }

        packagingOptions {
            jniLibs.useLegacyPackaging = true
        }
    }
}
