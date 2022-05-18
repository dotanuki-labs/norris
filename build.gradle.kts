import conventions.ignoredVulnerabilities

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
    id("com.osacky.doctor") version "0.8.0"
    id("org.sonatype.gradle.plugins.scan") version "2.3.0"
}

doctor {
    GCWarningThreshold.set(0.05f)
    javaHome {
        failOnError.set(false)
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
