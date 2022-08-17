dependencyResolutionManagement {
    versionCatalogs {
        create("deps") {
            from(files("../libs.versions.toml"))
        }
    }
}
