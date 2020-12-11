package plugins

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.ThemeType
import org.gradle.api.Project

fun Project.configureTestLogger() {
    val testLoggerExtension = extensions.findByName("testlogger") as TestLoggerExtension

    testLoggerExtension.apply {
        theme = ThemeType.MOCHA
    }
}
