import conventions.ignoredVulnerabilities
import kotlinx.kover.api.CoverageEngine
import kotlinx.kover.api.KoverTaskExtension

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
        classpath(Deps.shotGradlePlugin)
        classpath(Deps.keeperGradlePlugin)
    }
}

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
    id("com.osacky.doctor") version "0.8.0"
    id("io.github.cdsap.talaiot") version "1.5.1"
    id("org.sonatype.gradle.plugins.scan") version "2.2.3"
    id("org.jetbrains.kotlinx.kover") version "0.5.0"
}

doctor {
    GCWarningThreshold.set(0.05f)
    javaHome {
        failOnError.set(false)
    }
}

talaiot {
    metrics {
        gitMetrics = false
    }

    publishers {
        jsonPublisher = true
    }
}

detekt {
    config = files("$rootDir/detekt.yml")
    reports {
        xml.enabled = false
        html.enabled = false
        txt.enabled = false
    }
}

ossIndexAudit {
    isAllConfigurations = true
    isPrintBanner = false
    isShowAll = true
    excludeVulnerabilityIds = ignoredVulnerabilities()
}

kover {
    coverageEngine.set(CoverageEngine.INTELLIJ)
}

allprojects {

    repositories {
        mavenCentral()
        google()
    }

    tasks.withType<Test>() {
        extensions.configure<KoverTaskExtension>() {
            excludes = listOf(
                "io.dotanuki.norris.*.databinding.*",
                "io.dotanuki.norris.*.BuildConfig*"
            )
        }
    }
}

tasks.run {
    register("clean").configure {
        delete("build")
    }
}
