
plugins {
    id("io.dotanuki.gradle.automodule")
}

dependencies {
    implementation(projects.platform.android.coreNavigator)
    implementation(projects.platform.android.corePersistance)
    implementation(libs.androidx.appcompat)
}
