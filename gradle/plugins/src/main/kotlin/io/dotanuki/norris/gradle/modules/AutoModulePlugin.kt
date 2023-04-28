package io.dotanuki.norris.gradle.modules

import io.dotanuki.norris.gradle.modules.conventions.applyAndroidApplicationConventions
import io.dotanuki.norris.gradle.modules.conventions.applyAndroidFeatureLibraryConventions
import io.dotanuki.norris.gradle.modules.conventions.applyAndroidPlatformLibraryConventions
import io.dotanuki.norris.gradle.modules.conventions.applyKotlinProjectConventions
import io.dotanuki.norris.gradle.modules.conventions.applyTestLoggingConventions
import io.dotanuki.norris.gradle.modules.models.ModuleConvention
import io.dotanuki.norris.gradle.modules.models.ModuleConvention.ANDROID_APPLICATION
import io.dotanuki.norris.gradle.modules.models.ModuleConvention.ANDROID_FEATURE_LIBRARY
import io.dotanuki.norris.gradle.modules.models.ModuleConvention.ANDROID_PLATFORM_LIBRARY
import io.dotanuki.norris.gradle.modules.models.ModuleConvention.KOTLIN_PLATFORM_LIBRARY
import org.gradle.api.Plugin
import org.gradle.api.Project

class AutoModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.adarshr.test-logger")
            pluginManager.apply("org.gradle.test-retry")
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
                    pluginManager.apply("wtf.emulator.gradle")
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
