plugins {
    id("io.dotanuki.gradle.automodule")
}

dependencies {
    implementation(projects.platform.jvm.coreRest)
    implementation(projects.platform.jvm.coreKodein)
    implementation(projects.platform.android.corePersistance)
    implementation(projects.platform.android.coreNavigator)
    implementation(projects.platform.android.coreHelpers)
    implementation(projects.platform.android.coreAssets)
    implementation(projects.features.facts)
    implementation(projects.features.search)

    implementation(libs.kodein.di.jvm)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.jvm)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.square.okhttp.core)

    releaseImplementation(libs.square.leakcanary.release)

    androidTestImplementation(projects.platform.android.testingPersistance)
    androidTestImplementation(projects.platform.jvm.testingRest)

    androidTestImplementation(libs.junit4)
    androidTestImplementation(libs.google.truth)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.corektx)
    androidTestImplementation(libs.androidx.testext.junit)
    androidTestImplementation(libs.androidx.testext.junitktx)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.square.okhttp.mockwebserver)
    androidTestImplementation(libs.square.radiography)

    androidTestImplementation(libs.adevinta.barista) {
        exclude(group = "org.jetbrains.kotlin")
        exclude(group = "org.checkerframework")
    }
}
