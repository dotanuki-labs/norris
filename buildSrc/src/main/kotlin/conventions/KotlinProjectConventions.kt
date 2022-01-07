package conventions

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.applyKotlinProjectConventions() {
    tasks.run {

        withType<KotlinCompile>().configureEach {
            kotlinOptions.jvmTarget = "11"
            kotlinOptions.freeCompilerArgs += listOf(
                "-Xopt-in=kotlin.time.ExperimentalTime",
                "-Xopt-in=kotlin.RequiresOptIn"
            )
        }

        withType<JavaCompile>().configureEach {
            targetCompatibility = JavaVersion.VERSION_11.toString()
            sourceCompatibility = JavaVersion.VERSION_11.toString()
        }

        withType<Test>().configureEach {
            // Fix for retrofit `WARNING: Illegal reflective access by retrofit2.Platform`
            jvmArgs?.addAll(
                listOf("--add-opens", "java.base/java.lang.invoke=ALL-UNNAMED")
            )
        }
    }
}
