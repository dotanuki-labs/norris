package io.dotanuki.norris.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class FeatureMatrixPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val rootProject = target.rootProject
        rootProject.tasks
            .register("featureMatrix", FeatureMatrixTask::class.java)
            .configure {
                group = "Platform Tasks"
                description = "Extracts the list of features according Norris project conventions"
            }
    }
}
