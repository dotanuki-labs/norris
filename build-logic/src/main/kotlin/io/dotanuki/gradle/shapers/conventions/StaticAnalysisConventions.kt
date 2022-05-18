package io.dotanuki.gradle.shapers.conventions

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project

fun Project.applyStaticAnalysisConventions() {
    val detektExtension = extensions.findByName("detekt") as DetektExtension

    detektExtension.run {
        config = files("$rootDir/detekt.yml")
        reports {
            it.xml.enabled = false
            it.html.enabled = false
            it.txt.enabled = false
        }
    }
}
