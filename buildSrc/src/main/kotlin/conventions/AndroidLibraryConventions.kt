package conventions

import com.android.build.gradle.BaseExtension
import definitions.AndroidDefinitions
import definitions.ProguardDefinitions
import definitions.Versioning
import org.gradle.api.JavaVersion
import org.gradle.api.Project

fun Project.applyAndroidLibraryConventions() {
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

        buildTypes {
            getByName("release") {
                isMinifyEnabled = true

                val proguardConfig = ProguardDefinitions("$rootDir/app/proguard")
                proguardFiles(*(proguardConfig.customRules))
                proguardFiles(getDefaultProguardFile(proguardConfig.androidRules))
            }
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

    tasks.whenTaskAdded {
        if (name.startsWith("test") and name.contains("DebugUnitTest")) {
            enabled = false
        }
    }
}
