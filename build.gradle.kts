
import conventions.ignoredVulnerabilities
import org.sonatype.gradle.plugins.scan.ossindex.OutputFormat.JSON_CYCLONE_DX_1_4
import shadow.nexus.shadow.org.cyclonedx.model.Component.Type

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
        classpath(Deps.keeperGradlePlugin)
        classpath(Deps.dropshotsGradlePlugin)
    }
}

plugins {
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
    id("com.osacky.doctor") version "0.8.1"
    id("org.sonatype.gradle.plugins.scan") version "2.4.0"
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
    outputFormat = JSON_CYCLONE_DX_1_4
    cycloneDxComponentType = Type.APPLICATION
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
