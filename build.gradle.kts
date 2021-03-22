import com.vanniktech.android.junit.jacoco.JunitJacocoExtension
import io.gitlab.arturbosch.detekt.detekt

buildscript {

    repositories {
        google()
        jcenter()
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.31")
        classpath("com.adarshr:gradle-test-logger-plugin:2.1.1")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.4.31")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:9.4.0")
        classpath("com.vanniktech:gradle-android-junit-jacoco-plugin:0.16.0")
        classpath("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.14.2")
    }
}

allprojects {

    repositories {
        google()
        jcenter()
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://jitpack.io")
    }

    apply(plugin = BuildPlugins.Ids.detekt)
    apply(plugin = BuildPlugins.Ids.ktlint)
    apply(plugin = BuildPlugins.Ids.testLogger)

    detekt {
        config = files("$rootDir/default-detekt-config.yml")
    }
}

tasks.register("clean").configure {
    delete("build")
}

apply(plugin = BuildPlugins.Ids.jacocoUnified)

configure<JunitJacocoExtension> {
    jacocoVersion = "0.8.4"
}
