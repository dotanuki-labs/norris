package io.dotanuki.norris.gradle.modules.conventions

import io.dotanuki.norris.gradle.modules.models.PlatformDefinitions
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.applyKotlinProjectConventions() {

    val javaCompatibility = PlatformDefinitions.from(this).javaCompatibilityVersion.toString()

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
            kotlinOptions.jvmTarget = javaCompatibility
            kotlinOptions.freeCompilerArgs += kotlinCompilerFlags
            kotlinOptions.allWarningsAsErrors = true
        }

        withType<JavaCompile>().configureEach {
            targetCompatibility = javaCompatibility
            sourceCompatibility = javaCompatibility
        }

        withType<Test>().configureEach {
            jvmArgs?.addAll(jvmArgsAdditionalFlags)
        }
    }
}