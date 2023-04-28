plugins {
    id("io.dotanuki.gradle.automodule")
}

dependencies {
    implementation(projects.platform.jvm.coreRest)
    implementation(projects.platform.android.corePersistance)
    implementation(projects.platform.android.coreNavigator)
    implementation(projects.platform.android.coreAssets)
    implementation(projects.features.facts)
    implementation(projects.features.search)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.jvm)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.square.okhttp.core)
    implementation(libs.square.retrofit)
}
