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
        classpath(BuildPlugins.Dependencies.androidSupport)
        classpath(BuildPlugins.Dependencies.kotlinSupport)
        classpath(BuildPlugins.Dependencies.testLogger)
        classpath(BuildPlugins.Dependencies.kotlinxSerialization)
        classpath(BuildPlugins.Dependencies.ktlint)
        classpath(BuildPlugins.Dependencies.jacocoUnified)
        classpath(BuildPlugins.Dependencies.detekt)
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
