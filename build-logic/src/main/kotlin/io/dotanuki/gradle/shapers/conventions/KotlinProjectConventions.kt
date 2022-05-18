package io.dotanuki.gradle.shapers.conventions

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskCollection
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

inline fun <reified S : Task> TaskCollection<in S>.withType(): TaskCollection<S> =
    withType(S::class.java)

fun Project.applyKotlinProjectConventions() {
    tasks.run {

        withType<KotlinCompile>().configureEach { task ->
            task.kotlinOptions.jvmTarget = "11"
            task.kotlinOptions.freeCompilerArgs += listOf(
                "-Xopt-in=kotlin.time.ExperimentalTime",
                "-Xopt-in=kotlin.RequiresOptIn"
            )
        }

        withType<JavaCompile>().configureEach { task ->
            task.targetCompatibility = JavaVersion.VERSION_11.toString()
            task.sourceCompatibility = JavaVersion.VERSION_11.toString()
        }

        withType<Test>().configureEach { task ->
            // Fix for retrofit `WARNING: Illegal reflective access by retrofit2.Platform`
            task.jvmArgs?.addAll(
                listOf("--add-opens", "java.base/java.lang.invoke=ALL-UNNAMED")
            )
        }
    }
}
