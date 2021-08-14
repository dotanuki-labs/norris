package conventions

import com.android.build.api.dsl.BuildType
import com.android.build.gradle.BaseExtension
import definitions.AndroidDefinitions
import definitions.ProguardDefinitions
import definitions.Versioning
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import java.io.File
import java.io.FileInputStream
import java.util.Properties

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
        }
    }
}

fun Project.applyAndroidLibraryConventions() {
    applyAndroidStandardConventions()

    tasks.whenTaskAdded {
        if (name.startsWith("test") and name.contains("DebugUnitTest")) {
            enabled = false
        }
    }
}

fun Project.applyAndroidApplicationConventions() {
    applyAndroidStandardConventions()

    val android = extensions.findByName("android") as BaseExtension

    android.apply {
        signingConfigs {
            create("release") {
                loadSigningProperties().run {
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
                isTestCoverageEnabled = true
                configureHttps(this@applyAndroidApplicationConventions)
            }

            getByName("release") {
                isMinifyEnabled = true
                isShrinkResources = true

                val proguardConfig = ProguardDefinitions("$rootDir/app/proguard")
                proguardFiles(*(proguardConfig.customRules))
                proguardFiles(getDefaultProguardFile(proguardConfig.androidRules))
                signingConfig = signingConfigs.findByName("release")
                configureHttps(this@applyAndroidApplicationConventions)
            }
        }

        packagingOptions {
            jniLibs.useLegacyPackaging = true
        }
    }
}

private fun Project.httpEnabledForTesting(): Boolean =
    properties["testMode"]?.let { true } ?: false

private fun Project.evaluateAPIUrl(): String =
    properties["testMode"]?.let { "http://localhost:4242" } ?: "https://api.chucknorris.io"

private fun Project.evaluateTestBuildType(): String =
    properties["testMode"]?.let { "release" } ?: "debug"

private fun BuildType.configureHttps(project: Project) {
    buildConfigField("String", "CHUCKNORRIS_API_URL", "\"${project.evaluateAPIUrl()}\"")
    resValue("bool", "clear_networking_traffic_enabled", "${project.httpEnabledForTesting()}")
}

private fun Project.loadSigningProperties(): Properties =
    Properties().apply {
        load(FileInputStream("$rootDir/signing.properties"))
    }
