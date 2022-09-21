package io.dotanuki.norris.gradle.modules.models

import org.gradle.api.GradleException
import org.gradle.api.Project

internal enum class ModuleConvention {
    KOTLIN_PLATFORM_LIBRARY,
    ANDROID_PLATFORM_LIBRARY,
    ANDROID_FEATURE_LIBRARY,
    ANDROID_APPLICATION;

    companion object {
        private const val WRONG_LOCATION = "<module> not defined as :platform or :features sub-modules"

        fun from(target: Project): ModuleConvention {
            val path = target.projectDir.path

            return when {
                path.contains("app") -> ANDROID_APPLICATION
                path.contains("features/") -> ANDROID_FEATURE_LIBRARY
                path.contains("platform/jvm") -> KOTLIN_PLATFORM_LIBRARY
                path.contains("platform/android") -> ANDROID_PLATFORM_LIBRARY
                else -> throw GradleException(WRONG_LOCATION.replace("<module>", target.path))
            }
        }
    }
}
