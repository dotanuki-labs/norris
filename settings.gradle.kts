pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

include(
    ":app",
    ":platform:logger",
    ":platform:testing-commons",
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
