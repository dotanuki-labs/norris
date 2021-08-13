repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

plugins {
    java
    `kotlin-dsl`
    `java-gradle-plugin`
}

buildscript {
    repositories {
        google()
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:7.0.0")
    implementation("com.adarshr:gradle-test-logger-plugin:3.0.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("android-extensions"))
}

gradlePlugin {
    plugins {
        register("kotlin-module-plugin") {
            id = "norris.modules.kotlin"
            implementationClass = "plugins.NorrisKotlinModulePlugin"
        }

        register("android-platform-module-plugin") {
            id = "norris.modules.android.platform"
            implementationClass = "plugins.NorrisAndroidPlatformModulePlugin"
        }

        register("android-feature-module-plugin") {
            id = "norris.modules.android.feature"
            implementationClass = "plugins.NorrisAndroidFeatureModulePlugin"
        }
    }
}
