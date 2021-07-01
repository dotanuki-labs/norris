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
    ":platform:testing-app",
    ":platform:testing-commons",
    ":platform:testing-rest",
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
