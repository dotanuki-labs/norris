package io.dotanuki.gradle.shapers

import io.dotanuki.gradle.shapers.conventions.applySecurityConventions
import io.dotanuki.gradle.shapers.conventions.applyStaticAnalysisConventions
import io.dotanuki.gradle.shapers.definitions.PluginIdentifiers
import org.gradle.api.Plugin
import org.gradle.api.Project

class RootModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            if (parent != null) return@with

            plugins.apply(PluginIdentifiers.OSS_AUDIT)
            plugins.apply(PluginIdentifiers.KTLINT)
            plugins.apply(PluginIdentifiers.DETEKT)

            applySecurityConventions()
            applyStaticAnalysisConventions()
        }
    }
}
