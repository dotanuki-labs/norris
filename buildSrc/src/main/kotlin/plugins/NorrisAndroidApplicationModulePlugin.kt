package plugins

import conventions.applyAndroidApplicationConventions
import conventions.applyKotlinProjectConventions
import conventions.applyTestLoggingConventions
import org.gradle.api.Plugin
import org.gradle.api.Project

class NorrisAndroidApplicationModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            plugins.apply("com.android.application")
            plugins.apply("kotlin-android")
            plugins.apply("org.jlleitschuh.gradle.ktlint")
            plugins.apply("com.adarshr.test-logger")

            applyKotlinProjectConventions()
            applyTestLoggingConventions()
            applyAndroidApplicationConventions()
        }
    }
}
