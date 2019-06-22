package modules

object ModuleNames {

    const val MainApp = ":app"
    const val Domain = ":domain"
    const val Logger = ":logger"

    object Infrastructure {
        const val Networking = ":infrastructure:networking"
        const val Rest = ":infrastructure:rest-chucknorris"
    }

    object Features {
        const val Architecture = ":features:architecture"
        const val SharedAssets = ":features:shared-assets"
        const val Facts = ":features:facts"
    }
}