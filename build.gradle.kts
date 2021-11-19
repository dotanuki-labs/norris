import io.gitlab.arturbosch.detekt.detekt

buildscript {

    repositories {
        mavenCentral()
        google()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    }

    dependencies {
        classpath(Deps.androidGradlePlugin)
        classpath(Deps.kotlinGradlePlugin)
        classpath(Deps.testLoggerGradlePlugin)
        classpath(Deps.kotlinxSerializationGradlePlugin)
        classpath(Deps.ktlintGradlePlugin)
        classpath(Deps.detektGradlePlugin)
        classpath(Deps.shotGradlePlugin)
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
