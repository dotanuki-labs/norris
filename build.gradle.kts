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
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    id("io.gitlab.arturbosch.detekt") version "1.18.1"
    id("com.osacky.doctor") version "0.7.3"
    id("io.github.cdsap.talaiot") version "1.5.1"
    id("org.sonatype.gradle.plugins.scan") version "2.2.1"
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
    excludeCoordinates = setOf(
        "org.apache.ant:ant:1.10.9",
        "org.bouncycastle:bcprov-jdk15on:1.56",
        "org.apache.httpcomponents:httpclient:4.5.6",
        "org.apache.commons:commons-compress:1.21",
        "org.jsoup:jsoup:1.12.2",
        "junit:junit:4.12"
    )
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

tasks.register("clean").configure {
    delete("build")
}
