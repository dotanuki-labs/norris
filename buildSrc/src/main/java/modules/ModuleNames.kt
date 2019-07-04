package modules

object ModuleNames {

    const val MainApp = ":app"
    const val Domain = ":domain"
    const val Logger = ":logger"
    const val CoroutinesTestUtils = ":coroutines-testutils"

    object Infrastructure {
        const val Networking = ":networking"
        const val Persistance = ":persistance"
        const val Rest = ":rest-chucknorris"
    }

    object Features {
        const val Architecture = ":unidirectional-dataflow"
        const val SharedAssets = ":shared-assets"
        const val SharedUtilities = ":shared-utilities"
        const val Facts = ":facts"
        const val Search = ":search"
        const val Onboarding = ":onboarding"
        const val Navigator = ":navigator"
    }
}