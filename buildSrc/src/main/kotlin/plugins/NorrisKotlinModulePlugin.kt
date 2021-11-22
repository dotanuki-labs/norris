package plugins

import conventions.applyKotlinProjectConventions
import conventions.applyTestLoggingConventions
import org.gradle.api.Plugin
import org.gradle.api.Project

class NorrisKotlinModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            plugins.apply("kotlin")
            plugins.apply("com.adarshr.test-logger")

            applyKotlinProjectConventions()
            applyTestLoggingConventions()
        }
    }
}
