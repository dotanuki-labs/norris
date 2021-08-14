package conventions

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.applyKotlinProjectConventions() {
    tasks.run {
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "11"
            kotlinOptions.freeCompilerArgs += listOf(
                "-Xopt-in=kotlin.time.ExperimentalTime",
                "-Xopt-in=kotlin.RequiresOptIn"
            )
        }

        withType<Test>().configureEach {
            // Fix for retrofit `WARNING: Illegal reflective access by retrofit2.Platform`
            jvmArgs = listOf("--add-opens", "java.base/java.lang.invoke=ALL-UNNAMED")
        }
    }
}
