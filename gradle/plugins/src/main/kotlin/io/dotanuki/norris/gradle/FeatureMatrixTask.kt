package io.dotanuki.norris.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class FeatureMatrixTask : DefaultTask() {

    @TaskAction fun evaluateFeatures() {
        val features =
            project.rootProject.subprojects
                .filter { it.layout.projectDirectory.asFile.path.contains("features/") }
                .map { "\"" + it.layout.projectDirectory.asFile.name + "\"" }

        val buildDir = File("${project.rootProject.buildDir}/outputs/matrix").apply {
            if (!exists()) mkdirs()
        }

        val matrixFilePath = "$buildDir/features.json"
        val featureMatrixFile = File(matrixFilePath).apply { createNewFile() }
        featureMatrixFile.writeText(features.toString())

        logger.lifecycle("Wrote feature matrix file at $matrixFilePath")
    }
}
