
plugins {
    id("io.dotanuki.gradle.automodule")
}

dependencies {

    implementation(projects.platform.core.coreNavigator)
    implementation(projects.platform.core.coreRest)
    implementation(projects.platform.core.corePersistance)

    implementation(libs.kodein.di.jvm)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.square.okhttp.core)
    implementation(libs.robolectric)
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.test.corektx)
    implementation(libs.androidx.test.monitor)
}
