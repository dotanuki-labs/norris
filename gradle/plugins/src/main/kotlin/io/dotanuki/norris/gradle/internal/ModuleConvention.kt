package io.dotanuki.norris.gradle.internal

import org.gradle.api.GradleException
import org.gradle.api.Project
import java.io.File

enum class ModuleConvention {
    KOTLIN_PLATFORM_LIBRARY,
    ANDROID_PLATFORM_LIBRARY,
    ANDROID_FEATURE_LIBRARY,
    ANDROID_APPLICATION;

    companion object {

        private const val MISSING_DEFINITION = "Expecting a '.automodule' file all :platform sub-modules"
        private const val WRONG_DEFINTION =
            "Expecting 'kotlin-library' or 'android-library' as definition, not <definition>"
        private const val WRONG_LOCATION = "<module> not defined as :platform or :features sub-modules"

        fun from(target: Project): ModuleConvention {
            val path = target.projectDir.path

            return when {
                path.contains("app") -> ANDROID_APPLICATION
                path.contains("features/") -> ANDROID_FEATURE_LIBRARY
                path.contains("platform/") -> {
                    val optIn = File("$path/.automodule")
                    println(optIn)

                    if (!optIn.exists()) {
                        throw GradleException(MISSING_DEFINITION)
                    }

                    when (val definition = optIn.readText().replace("\n", "").trim()) {
                        "kotlin-library" -> KOTLIN_PLATFORM_LIBRARY
                        "android-library" -> ANDROID_PLATFORM_LIBRARY
                        else -> throw GradleException(WRONG_DEFINTION.replace("<definition>", definition))
                    }
                }
                else -> throw GradleException(WRONG_LOCATION.replace("<module>", target.path))
            }
        }
    }
}

