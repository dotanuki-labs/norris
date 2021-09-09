import io.gitlab.arturbosch.detekt.detekt

buildscript {

    repositories {
        mavenCentral()
        google()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30")
        classpath("com.adarshr:gradle-test-logger-plugin:3.0.0")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.5.30")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:10.2.0")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.18.1")
        classpath("com.karumi:shot:5.11.2")
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
