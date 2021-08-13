package plugins

import conventions.applyAndroidLibraryConventions
import conventions.applyKotlinProjectConventions
import conventions.applyTestLoggingConventions
import org.gradle.api.Plugin
import org.gradle.api.Project

class NorrisAndroidPlatformModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            plugins.apply("kotlin-android")
            plugins.apply("com.android.library")
            plugins.apply("org.jlleitschuh.gradle.ktlint")
            plugins.apply("com.adarshr.test-logger")

            applyKotlinProjectConventions()
            applyTestLoggingConventions()
            applyAndroidLibraryConventions()
        }
    }
}
