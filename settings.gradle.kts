rootProject.name = "norris"

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    includeBuild("gradle/plugins")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.8.0")
}

include(
    // Product
    ":app",
    ":platform:jvm:core-rest",
    ":platform:jvm:testing-mockserver",
    ":platform:jvm:testing-rest",
    ":platform:android:core-assets",
    ":platform:android:core-navigator",
    ":platform:android:core-persistence",
    ":platform:android:testing-application",
    ":platform:android:testing-helpers-espresso",
    ":platform:android:testing-persistence",
    ":platform:android:testing-screenshots",
    ":features:facts",
    ":features:search",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
