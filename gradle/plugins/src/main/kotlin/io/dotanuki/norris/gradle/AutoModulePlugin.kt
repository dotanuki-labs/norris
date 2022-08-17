package io.dotanuki.norris.gradle

import io.dotanuki.norris.gradle.internal.ModuleConvention
import io.dotanuki.norris.gradle.internal.ModuleConvention.ANDROID_APPLICATION
import io.dotanuki.norris.gradle.internal.ModuleConvention.ANDROID_FEATURE_LIBRARY
import io.dotanuki.norris.gradle.internal.ModuleConvention.ANDROID_PLATFORM_LIBRARY
import io.dotanuki.norris.gradle.internal.ModuleConvention.KOTLIN_PLATFORM_LIBRARY
import io.dotanuki.norris.gradle.internal.applyKotlinProjectConventions
import io.dotanuki.norris.gradle.internal.applyTestLoggingConventions
import org.gradle.api.Plugin
import org.gradle.api.Project

class AutoModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {

        when (ModuleConvention.from(target)) {
            KOTLIN_PLATFORM_LIBRARY -> {
                target.applyTestLoggingConventions()
                target.applyKotlinProjectConventions()
            }
            ANDROID_PLATFORM_LIBRARY -> {
                TODO()
            }
            ANDROID_FEATURE_LIBRARY -> {
                TODO()
            }
            ANDROID_APPLICATION -> {
                TODO()
            }
        }
    }
}
