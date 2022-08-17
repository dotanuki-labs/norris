package io.dotanuki.norris.gradle.internal

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.ThemeType
import org.gradle.api.Project

internal fun Project.applyTestLoggingConventions() {

    pluginManager.apply("com.adarshr.test-logger")

    val testLoggerExtension = extensions.findByName("testlogger") as TestLoggerExtension

    testLoggerExtension.apply {
        theme = ThemeType.MOCHA_PARALLEL
    }
}
