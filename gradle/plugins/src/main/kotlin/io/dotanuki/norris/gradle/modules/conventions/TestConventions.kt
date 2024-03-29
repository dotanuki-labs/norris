package io.dotanuki.norris.gradle.modules.conventions

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.ThemeType
import org.gradle.api.Project

internal fun Project.applyTestLoggingConventions() {

    val testLoggerExtension = extensions.findByName("testlogger") as TestLoggerExtension

    testLoggerExtension.apply {
        theme = ThemeType.MOCHA_PARALLEL
    }
}
