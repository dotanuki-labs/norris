package plugins

import BuildPlugins
import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.ThemeType
import configs.KotlinConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class SetupKotlinModulePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            plugins.apply(BuildPlugins.Ids.kotlinJVM)
            plugins.apply(BuildPlugins.Ids.testLogger)

            tasks.withType<KotlinCompile>().configureEach {
                kotlinOptions.jvmTarget = KotlinConfig.targetJVM
            }

            val testLoggerExtension = extensions.findByName("testlogger") as TestLoggerExtension

            testLoggerExtension.apply {
                theme = ThemeType.MOCHA
            }
        }
    }
}