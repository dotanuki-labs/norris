package io.dotanuki.gradle.shapers

import io.dotanuki.gradle.shapers.conventions.applyAndroidLibraryConventions
import io.dotanuki.gradle.shapers.conventions.applyKotlinProjectConventions
import io.dotanuki.gradle.shapers.conventions.applyScreenshotTestsConventions
import io.dotanuki.gradle.shapers.conventions.applyTestLoggingConventions
import io.dotanuki.gradle.shapers.definitions.PluginIdentifiers
import org.gradle.api.Plugin
import org.gradle.api.Project

class FeatureModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            plugins.apply(PluginIdentifiers.AGP_LIBRARY)
            plugins.apply(PluginIdentifiers.KOTLIN_ANDROID)
            plugins.apply(PluginIdentifiers.TESTLOGGER)
            plugins.apply(PluginIdentifiers.SHOT)

            applyKotlinProjectConventions()
            applyTestLoggingConventions()
            applyAndroidLibraryConventions()
            applyScreenshotTestsConventions()
        }
    }
}
