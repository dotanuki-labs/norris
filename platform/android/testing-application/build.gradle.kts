
plugins {
    id("io.dotanuki.gradle.automodule")
}

dependencies {
    implementation(projects.platform.android.coreNavigator)
    implementation(projects.platform.android.corePersistence)
    implementation(libs.androidx.appcompat)
}
