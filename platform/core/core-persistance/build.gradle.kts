
plugins {
    id("norris.modules.android.platform")
}

dependencies {

    implementation(libs.kodein.di.jvm)
    implementation(libs.kodein.type.jvm)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit4)
    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.jvm)
}
