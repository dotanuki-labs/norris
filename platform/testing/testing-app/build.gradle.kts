
plugins {
    id("norris.modules.android.platform")
}

dependencies {

    implementation(projects.platform.core.coreNavigator)
    implementation(projects.platform.core.coreRest)
    implementation(projects.platform.core.corePersistance)

    implementation(libs.kodein.di.jvm)
    implementation(libs.kodein.type.jvm)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.okhttp.core)
    implementation(libs.robolectric)
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.test.corektx)
    implementation(libs.androidx.test.monitor)
}
