package io.dotanuki.norris.gradle.modules.conventions

import io.dotanuki.norris.gradle.modules.models.PlatformDefinitions
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.applyKotlinProjectConventions() {

    val platform = PlatformDefinitions.from(this)
    val javaCompatibility = platform.javaCompatibilityVersion.toString()

    val kotlinCompilerFlags = listOf(
        "-opt-in=kotlin.time.ExperimentalTime",
        "-opt-in=kotlin.RequiresOptIn",
        "-Xcontext-receivers"
    )

    kotlinExtension.jvmToolchain {
        languageVersion.set(platform.targetJdkVersion)
        vendor.set(platform.targetJdkVendor)
    }

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
    }
}
