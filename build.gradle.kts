buildscript {

    repositories {
        mavenCentral()
        google()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.20")
        classpath("com.adarshr:gradle-test-logger-plugin:3.0.0")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.5.20")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:10.1.0")
    }
}

allprojects {

    repositories {
        mavenCentral()
        google()
        jcenter()
        maven(url = "https://jitpack.io")
    }

    apply(plugin = BuildPlugins.Ids.ktlint)
    apply(plugin = BuildPlugins.Ids.testLogger)
}

tasks.register("clean").configure {
    delete("build")
}
