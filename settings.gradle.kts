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
    ":platform:core:navigator",
    ":platform:core:networking",
    ":platform:core:persistance",
    ":platform:core:rest-chucknorris",
    ":platform:common:common-static",
    ":platform:common:common-android",
    ":platform:common:common-kodein",
    ":features:facts",
    ":features:search",
    ":features:onboarding"
)
