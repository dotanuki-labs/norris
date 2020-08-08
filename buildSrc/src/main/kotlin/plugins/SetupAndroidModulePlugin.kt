package plugins

import BuildPlugins
import Versioning
import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.ThemeType
import com.android.build.gradle.BaseExtension
import configs.AndroidConfig
import configs.KotlinConfig
import configs.ProguardConfig
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class SetupAndroidModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            plugins.apply(BuildPlugins.Ids.androidLibrary)
            plugins.apply(BuildPlugins.Ids.kotlinAndroid)
            plugins.apply(BuildPlugins.Ids.kotlinAndroidExtensions)
            plugins.apply(BuildPlugins.Ids.testLogger)

            tasks.withType<KotlinCompile>().configureEach {
                kotlinOptions.jvmTarget = KotlinConfig.targetJVM
            }

            val testLogger = extensions.findByName("testlogger") as TestLoggerExtension

            testLogger.apply {
                theme = ThemeType.MOCHA
            }

            val android = extensions.findByName("android") as BaseExtension

            android.apply {
                compileSdkVersion(AndroidConfig.compileSdk)
                buildToolsVersion(AndroidConfig.buildToolsVersion)

                defaultConfig {

                    minSdkVersion(AndroidConfig.minSdk)
                    targetSdkVersion(AndroidConfig.targetSdk)
                    versionCode = Versioning.version.code
                    versionName = Versioning.version.name

                    vectorDrawables.apply {
                        useSupportLibrary = true
                        generatedDensities(*(AndroidConfig.noGeneratedDensities))
                    }

                    resConfig("en")
                }

                buildTypes {
                    getByName("debug") {
                        isTestCoverageEnabled = true
                    }

                    getByName("release") {
                        isMinifyEnabled = true

                        val proguardConfig = ProguardConfig("$rootDir/proguard")
                        proguardFiles(*(proguardConfig.customRules))
                        proguardFiles(getDefaultProguardFile(proguardConfig.androidRules))
                    }
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
                }

                tasks.withType<KotlinCompile> {
                    kotlinOptions.jvmTarget = KotlinConfig.targetJVM
                }

                testOptions {
                    unitTests.isReturnDefaultValues = true
                    unitTests.isIncludeAndroidResources = true
                }
            }

            val androidExtensions = extensions.findByName("androidExtensions") as AndroidExtensionsExtension

            androidExtensions.apply {
                isExperimental = true
            }
        }
    }
}