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
    }
}

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    id("io.gitlab.arturbosch.detekt") version "1.20.0"
}

detekt {
    config = files("$rootDir/detekt.yml")
    reports {
        xml.enabled = false
        html.enabled = false
        txt.enabled = false
    }
}

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
