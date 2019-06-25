import com.vanniktech.android.junit.jacoco.JunitJacocoExtension

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
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://kotlin.bintray.com/kotlinx")
    }
}

tasks.register("clean").configure {
    delete("build")
}

apply(plugin = BuildPlugins.Ids.jacocoUnified)
configure<JunitJacocoExtension> {
    jacocoVersion = "0.8.4"
}