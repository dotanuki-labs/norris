package io.dotanuki.gradle.shapers

import io.dotanuki.gradle.shapers.conventions.applyAndroidApplicationConventions
import io.dotanuki.gradle.shapers.conventions.applyKotlinProjectConventions
import io.dotanuki.gradle.shapers.conventions.applyTestLoggingConventions
import io.dotanuki.gradle.shapers.conventions.isTestMode
import io.dotanuki.gradle.shapers.definitions.PluginIdentifiers
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidAppModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            plugins.apply(PluginIdentifiers.AGP_APP)
            plugins.apply(PluginIdentifiers.KOTLIN_ANDROID)
            plugins.apply(PluginIdentifiers.KEEPER)

            applyKotlinProjectConventions()
            applyAndroidApplicationConventions()
        }
    }
}
