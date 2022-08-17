package io.dotanuki.norris.gradle

import io.dotanuki.norris.gradle.internal.ModuleConvention
import io.dotanuki.norris.gradle.internal.ModuleConvention.ANDROID_APPLICATION
import io.dotanuki.norris.gradle.internal.ModuleConvention.ANDROID_FEATURE_LIBRARY
import io.dotanuki.norris.gradle.internal.ModuleConvention.ANDROID_PLATFORM_LIBRARY
import io.dotanuki.norris.gradle.internal.ModuleConvention.KOTLIN_PLATFORM_LIBRARY
import io.dotanuki.norris.gradle.internal.conventions.applyAndroidApplicationConventions
import io.dotanuki.norris.gradle.internal.conventions.applyAndroidFeatureLibraryConventions
import io.dotanuki.norris.gradle.internal.conventions.applyAndroidPlatformLibraryConventions
import io.dotanuki.norris.gradle.internal.conventions.applyKotlinProjectConventions
import io.dotanuki.norris.gradle.internal.conventions.applyTestLoggingConventions
import org.gradle.api.Plugin
import org.gradle.api.Project

class AutoModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.adarshr.test-logger")
            target.applyTestLoggingConventions()

            when (ModuleConvention.from(target)) {
                KOTLIN_PLATFORM_LIBRARY -> {
                    pluginManager.apply("kotlin")
                    applyKotlinProjectConventions()
                }
                ANDROID_PLATFORM_LIBRARY -> {
                    pluginManager.apply("kotlin-android")
                    pluginManager.apply("com.android.library")
                    applyKotlinProjectConventions()
                    applyAndroidPlatformLibraryConventions()
                }
                ANDROID_FEATURE_LIBRARY -> {
                    pluginManager.apply("kotlin-android")
                    pluginManager.apply("com.android.library")
                    pluginManager.apply("com.dropbox.dropshots")
                    applyKotlinProjectConventions()
                    applyAndroidFeatureLibraryConventions()
                }
                ANDROID_APPLICATION -> {
                    pluginManager.apply("kotlin-android")
                    pluginManager.apply("com.android.application")
                    applyKotlinProjectConventions()
                    applyAndroidApplicationConventions()
                }
            }
        }
    }
}
