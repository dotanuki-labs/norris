package io.dotanuki.gradle.shapers

import io.dotanuki.gradle.shapers.conventions.applyKotlinProjectConventions
import io.dotanuki.gradle.shapers.conventions.applyTestLoggingConventions
import io.dotanuki.gradle.shapers.definitions.PluginIdentifiers
import org.gradle.api.Plugin
import org.gradle.api.Project

class PlatformKotlinModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            plugins.apply(PluginIdentifiers.KOTLIN_JVM)
            plugins.apply(PluginIdentifiers.TESTLOGGER)
            applyKotlinProjectConventions()
            applyTestLoggingConventions()
        }
    }
}
