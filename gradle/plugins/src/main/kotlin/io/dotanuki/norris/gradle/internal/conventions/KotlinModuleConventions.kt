package io.dotanuki.norris.gradle.internal.conventions

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.applyKotlinProjectConventions() {

    val kotlinCompilerFlags = listOf(
        "-opt-in=kotlin.time.ExperimentalTime",
        "-opt-in=kotlin.RequiresOptIn"
    )

    // Fix for retrofit :
    // WARNING: Illegal reflective access by retrofit2.Platform
    val jvmArgsAdditionalFlags = listOf(
        "--add-opens",
        "java.base/java.lang.invoke=ALL-UNNAMED"
    )

    tasks.run {

        withType<KotlinCompile>().configureEach {
            kotlinOptions.jvmTarget = "11"
            kotlinOptions.freeCompilerArgs += kotlinCompilerFlags
            kotlinOptions.allWarningsAsErrors = true
        }

        withType<JavaCompile>().configureEach {
            targetCompatibility = JavaVersion.VERSION_11.toString()
            sourceCompatibility = JavaVersion.VERSION_11.toString()
        }

        withType<Test>().configureEach {
            jvmArgs?.addAll(jvmArgsAdditionalFlags)
        }
    }
}
