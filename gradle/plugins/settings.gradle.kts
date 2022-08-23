rootProject.name = "gradle-plugins"

dependencyResolutionManagement {
    versionCatalogs {
        create("deps") {
            from(files("../libs.versions.toml"))
        }
    }
}
