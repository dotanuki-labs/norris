
plugins {
    id("io.dotanuki.gradle.automodule")
}

dependencies {
    implementation(projects.platform.common.commonKodein)

    implementation(libs.kodein.di.jvm)
    implementation(libs.androidx.appcompat)

    testImplementation(projects.platform.testing.testingCommons)
    testImplementation(libs.google.truth)
    testImplementation(libs.junit4)
    testImplementation(libs.robolectric)
}
