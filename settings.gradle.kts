pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

include(
    ":app",
    ":platform:testing:testing-app",
    ":platform:testing:testing-commons",
    ":platform:testing:testing-rest",
    ":platform:testing:testing-persistance",
    ":platform:testing:testing-screenshots",
    ":platform:core:core-navigator",
    ":platform:core:core-networking",
    ":platform:core:core-persistance",
    ":platform:core:core-rest",
    ":platform:common:common-static",
    ":platform:common:common-android",
    ":platform:common:common-kodein",
    ":features:facts",
    ":features:search"
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
