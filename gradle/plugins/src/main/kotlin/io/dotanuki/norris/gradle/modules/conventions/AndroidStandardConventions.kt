package io.dotanuki.norris.gradle.modules.conventions

import com.android.build.gradle.BaseExtension
import io.dotanuki.norris.gradle.modules.models.PlatformDefinitions
import io.dotanuki.norris.gradle.modules.models.Versioning
import org.gradle.api.Project
import java.util.Collections

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
        compileSdkVersion(platformDefinitions.androidCompileSdk)
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
