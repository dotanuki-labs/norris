package plugins

import Versioning
import com.android.build.gradle.BaseExtension
import configs.AndroidConfig
import configs.ProguardConfig
import org.gradle.api.JavaVersion
import org.gradle.api.Project

fun Project.configureAsAndroidLibrary() {
    val android = extensions.findByName("android") as BaseExtension

    android.apply {
        compileSdkVersion(configs.AndroidConfig.compileSdk)
        buildToolsVersion(configs.AndroidConfig.buildToolsVersion)

        defaultConfig {

            minSdkVersion(AndroidConfig.minSdk)
            targetSdkVersion(configs.AndroidConfig.targetSdk)
            versionCode = Versioning.version.code
            versionName = Versioning.version.name

            vectorDrawables.apply {
                useSupportLibrary = true
                generatedDensities(*(configs.AndroidConfig.noGeneratedDensities))
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

        testOptions {
            unitTests.isReturnDefaultValues = true
            unitTests.isIncludeAndroidResources = true
        }
    }
}
