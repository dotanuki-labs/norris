
plugins {
    id("io.dotanuki.gradle.automodule")
}

dependencies {

    implementation(projects.platform.jvm.coreRest)
    implementation(projects.platform.android.coreNavigator)
    implementation(projects.platform.android.corePersistance)

    implementation(libs.kodein.di.jvm)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.square.okhttp.core)
    implementation(libs.robolectric)
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.test.corektx)
    implementation(libs.androidx.test.monitor)
}
