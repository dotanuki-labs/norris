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
        compileSdkVersion(AndroidConfig.compileSdk)
        buildToolsVersion(AndroidConfig.buildToolsVersion)

        defaultConfig {

            minSdk = AndroidConfig.minSdk
            targetSdk = AndroidConfig.targetSdk
            versionCode = Versioning.version.code
            versionName = Versioning.version.name

            vectorDrawables.apply {
                useSupportLibrary = true
                generatedDensities(*(AndroidConfig.noGeneratedDensities))
            }

            resourceConfigurations.add("en")
        }

        buildTypes {
            getByName("release") {
                isMinifyEnabled = true

                val proguardConfig = ProguardConfig("$rootDir/app/proguard")
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

    tasks.whenTaskAdded {
        if (name.startsWith("test") and name.contains("DebugUnitTest")) {
            enabled = false
        }
    }
}
