package io.dotanuki.norris.gradle.internal

import io.dotanuki.norris.gradle.internal.ModuleConvention.ANDROID_APPLICATION
import io.dotanuki.norris.gradle.internal.ModuleConvention.ANDROID_FEATURE_LIBRARY
import io.dotanuki.norris.gradle.internal.ModuleConvention.ANDROID_PLATFORM_LIBRARY
import io.dotanuki.norris.gradle.internal.ModuleConvention.KOTLIN_PLATFORM_LIBRARY
import org.gradle.api.Project

internal class ConventionsApplier(private val target: Project) {

    fun selectAndApply(convention: ModuleConvention) {

        target.applyTestLoggingConventions()

        when (convention) {
            KOTLIN_PLATFORM_LIBRARY -> target.applyKotlinProjectConventions()
            ANDROID_PLATFORM_LIBRARY -> TODO()
            ANDROID_FEATURE_LIBRARY -> TODO()
            ANDROID_APPLICATION -> TODO()
        }
    }
}
