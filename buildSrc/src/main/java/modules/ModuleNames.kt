package modules

object ModuleNames {

    const val MainApp = ":app"
    const val Domain = ":domain"
    const val Logger = ":logger"
    const val CoroutinesTestUtils = ":coroutines-testutils"

    object Infrastructure {
        const val Networking = ":infrastructure:networking"
        const val Persistance = ":infrastructure:persistance"
        const val Rest = ":infrastructure:rest-chucknorris"
    }

    object Features {
        const val Architecture = ":features:architecture"
        const val SharedAssets = ":features:shared-assets"
        const val SharedUtilities = ":features:shared-utilities"
        const val Facts = ":features:facts"
        const val Search = ":features:search"
        const val Navigator = ":features:navigator"
    }
}