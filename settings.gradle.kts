pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(
    ":app",
    ":platform:domain",
    ":platform:logger",
    ":platform:coroutines-testutils",
    ":platform:navigator",
    ":platform:networking",
    ":platform:persistance",
    ":platform:rest-chucknorris",
    ":platform:shared-assets",
    ":platform:shared-utilities",
    ":features:facts",
    ":features:search",
    ":features:onboarding"
)
