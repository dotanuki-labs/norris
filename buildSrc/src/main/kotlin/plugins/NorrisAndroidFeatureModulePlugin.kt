package plugins

import conventions.applyAndroidLibraryConventions
import conventions.applyKotlinProjectConventions
import conventions.applyScreenshotTestsConventions
import conventions.applyTestLoggingConventions
import org.gradle.api.Plugin
import org.gradle.api.Project

class NorrisAndroidFeatureModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            plugins.apply("com.android.library")
            plugins.apply("kotlin-android")
            plugins.apply("com.adarshr.test-logger")
            plugins.apply("shot")

            applyKotlinProjectConventions()
            applyTestLoggingConventions()
            applyAndroidLibraryConventions()
            applyScreenshotTestsConventions()
        }
    }
}
