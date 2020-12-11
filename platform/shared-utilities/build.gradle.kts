
plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {
    implementation(Libraries.kotlinStdlib)
    implementation(Libraries.kodein)
    implementation(Libraries.appCompat)
    implementation(Libraries.lifecycleExtensions)
    implementation(Libraries.lifecycleViewModel)
}
