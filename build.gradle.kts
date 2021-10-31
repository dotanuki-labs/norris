import io.gitlab.arturbosch.detekt.detekt

buildscript {

    repositories {
        mavenCentral()
        google()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    }

    dependencies {
        classpath(libs.android.gradle)
        classpath(libs.kotlin.lang.gradle)
        classpath(libs.kotlinx.serialization.gradle)
        classpath(libs.testlogger.gradle)
        classpath(libs.ktlint.gradle)
        classpath(libs.detekt.gradle)
        classpath(libs.shot.gradle)
    }
}

plugins {
    id("com.osacky.doctor") version "0.7.3"
    id("io.github.cdsap.talaiot") version "1.5.1"
}

doctor {
    GCWarningThreshold.set(0.05f)
}

talaiot {
    metrics {
        gitMetrics = false
    }

    publishers {
        jsonPublisher = true
    }
}

allprojects {

    repositories {
        mavenCentral()
        google()
    }

    apply(plugin = "io.gitlab.arturbosch.detekt")

    detekt {
        config = files("$rootDir/detekt.yml")
        reports {
            xml.enabled = false
            html.enabled = false
            txt.enabled = false
        }
    }
}

tasks.register("clean").configure {
    delete("build")
}
