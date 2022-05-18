buildscript {

    repositories {
        mavenCentral()
        google()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    }

    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.testlogger.gradle.plugin)
        classpath(libs.kotlinx.serialization.gradle.plugin)
        classpath(libs.shot.gradle.plugin)
        classpath(libs.keeper.gradle.plugin)
        classpath(libs.oss.audit.gradle.plugin)
        classpath(libs.detekt.gradle.plugin)
        classpath(libs.ktlint.gradle.plugin)
    }
}

apply(plugin = "norris.plugins.shapers.root")

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

tasks.run {
    register("clean").configure {
        delete("build")
    }
}
