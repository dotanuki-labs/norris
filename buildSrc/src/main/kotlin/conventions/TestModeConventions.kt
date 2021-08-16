package conventions

import org.gradle.api.Project

fun Project.isTestMode(): Boolean = properties["testMode"]?.let { true } ?: false
