plugins {
    id("norris.modules.android.platform")
}

dependencies {
    implementation(projects.platform.common.commonKodein)

    implementation(libs.kodein.di.jvm)
    implementation(libs.kodein.type.jvm)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.kotlinx.coroutines.jvm)
}
