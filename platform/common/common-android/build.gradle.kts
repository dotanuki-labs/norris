plugins {
    id("io.dotanuki.gradle.automodule")
}

dependencies {
    implementation(projects.platform.common.commonKodein)

    implementation(libs.kodein.di.jvm)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.kotlinx.coroutines.jvm)
}
