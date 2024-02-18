
plugins {
    id("io.dotanuki.gradle.automodule")
}

dependencies {
    implementation(libs.androidx.appcompat)

    testImplementation(libs.google.truth)
    testImplementation(libs.junit4)
    testImplementation(libs.robolectric)
}
