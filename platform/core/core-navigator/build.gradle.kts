
plugins {
    id("norris.modules.android.platform")
}

dependencies {
    implementation(projects.platform.common.commonKodein)

    implementation(libs.kodein.di.jvm)
    implementation(libs.kodein.type.jvm)
    implementation(libs.androidx.appcompat)

    testImplementation(projects.platform.testing.testingCommons)
    testImplementation(libs.junit4)
    testImplementation(libs.truth)
    testImplementation(libs.robolectric)
}
