package io.dotanuki.norris.gradle.modules.conventions

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.ThemeType
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.retry
import org.gradle.kotlin.dsl.withType

internal fun Project.applyTestLoggingConventions() {

    val testLoggerExtension = extensions.findByName("testlogger") as TestLoggerExtension

    testLoggerExtension.apply {
        theme = ThemeType.MOCHA_PARALLEL
    }

    tasks.withType<Test>().configureEach {
        retry {
            maxRetries.set(3)
            failOnPassedAfterRetry.set(true)
        }
    }
}
