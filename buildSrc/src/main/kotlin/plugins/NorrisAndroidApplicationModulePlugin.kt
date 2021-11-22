package plugins

import conventions.applyAndroidApplicationConventions
import conventions.applyKotlinProjectConventions
import conventions.applyTestLoggingConventions
import conventions.isTestMode
import org.gradle.api.Plugin
import org.gradle.api.Project

class NorrisAndroidApplicationModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            plugins.apply("com.android.application")
            plugins.apply("kotlin-android")
            plugins.apply("com.adarshr.test-logger")

            if (target.isTestMode()) {
                plugins.apply("com.slack.keeper")
            }

            applyKotlinProjectConventions()
            applyTestLoggingConventions()
            applyAndroidApplicationConventions()
        }
    }
}
