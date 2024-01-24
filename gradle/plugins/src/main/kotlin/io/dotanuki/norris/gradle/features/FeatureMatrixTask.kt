package io.dotanuki.norris.gradle.features

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.File

@CacheableTask
abstract class FeatureMatrixTask : DefaultTask() {
    @get:InputFiles @get:PathSensitive(PathSensitivity.RELATIVE) abstract val featureModules: ConfigurableFileCollection

    @get:OutputFile abstract val matrixFile: RegularFileProperty

    init {
        val features =
            project.rootProject.subprojects.filter {
                it.layout.projectDirectory.asFile.path.contains("features/")
            }

        featureModules.from(features.map { it.layout.projectDirectory.asFile })
        matrixFile.set(project.matrixOutputFile())
    }

    @TaskAction fun evaluateFeatures() {
        val featuresJson = featureModules.map { "\"" + it.name + "\"" }

        project.matrixOutputDir().run {
            if (!exists()) mkdirs()
        }

        val featureMatrixFile = matrixFile.get().asFile.apply { createNewFile() }
        featureMatrixFile.writeText(featuresJson.toString())

        logger.lifecycle("Wrote feature matrix file at $featureMatrixFile")
    }

    private fun Project.matrixOutputDir(): File = File("${project.rootProject.buildDir}/outputs/matrix")

    private fun Project.matrixOutputFile(): File = File("${matrixOutputDir()}/features.json")
}
