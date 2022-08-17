package io.dotanuki.norris.gradle

import io.dotanuki.norris.gradle.internal.ModuleConvention
import io.dotanuki.norris.gradle.internal.ModuleConvention.ANDROID_APPLICATION
import io.dotanuki.norris.gradle.internal.ModuleConvention.ANDROID_FEATURE_LIBRARY
import io.dotanuki.norris.gradle.internal.ModuleConvention.ANDROID_PLATFORM_LIBRARY
import io.dotanuki.norris.gradle.internal.ModuleConvention.KOTLIN_PLATFORM_LIBRARY
import io.dotanuki.norris.gradle.internal.applyAndroidFeatureLibraryConventions
import io.dotanuki.norris.gradle.internal.applyAndroidPlatformLibraryConventions
import io.dotanuki.norris.gradle.internal.applyKotlinProjectConventions
import io.dotanuki.norris.gradle.internal.applyTestLoggingConventions
import org.gradle.api.Plugin
import org.gradle.api.Project

class AutoModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {

            pluginManager.apply("com.adarshr.test-logger")


            when (ModuleConvention.from(target)) {
                KOTLIN_PLATFORM_LIBRARY -> {
                    pluginManager.apply("kotlin")

                    target.applyTestLoggingConventions()
                    target.applyKotlinProjectConventions()
                }
                ANDROID_PLATFORM_LIBRARY -> {
                    pluginManager.apply("kotlin-android")
                    pluginManager.apply("com.android.library")
                    pluginManager.apply("com.adarshr.test-logger")

                    target.applyTestLoggingConventions()
                    target.applyKotlinProjectConventions()
                    target.applyAndroidPlatformLibraryConventions()
                }
                ANDROID_FEATURE_LIBRARY -> {
                    pluginManager.apply("kotlin-android")
                    pluginManager.apply("com.android.library")
                    pluginManager.apply("com.adarshr.test-logger")
                    pluginManager.apply("com.dropbox.dropshots")

                    target.applyTestLoggingConventions()
                    target.applyKotlinProjectConventions()
                    target.applyAndroidFeatureLibraryConventions()
                }
                ANDROID_APPLICATION -> {
                    TODO()
                }
            }
        }
    }
}
