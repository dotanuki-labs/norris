
plugins {
    id("io.dotanuki.gradle.automodule")
}

dependencies {
    implementation(projects.platform.jvm.coreKodein)
    implementation(projects.platform.jvm.coreRest)
    implementation(projects.platform.android.coreHelpers)
    implementation(projects.platform.android.coreAssets)
    implementation(projects.platform.android.corePersistance)

    implementation(libs.kodein.di.jvm)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.jvm)
    implementation(libs.androidx.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.vm)
    implementation(libs.androidx.lifecycle.vm.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.swipetorefresh)
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.google.material.design)

    testImplementation(projects.platform.android.testingApp)
    testImplementation(projects.platform.android.testingPersistance)
    testImplementation(projects.platform.jvm.testingRest)

    testImplementation(libs.junit4)
    testImplementation(libs.google.truth)
    testImplementation(libs.square.okhttp.mockwebserver)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.testext.junit)
    testImplementation(libs.robolectric)

    androidTestImplementation(projects.platform.jvm.coreNetworking)
    androidTestImplementation(projects.platform.android.testingScreenshots)

    androidTestImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.corektx)
    androidTestImplementation(libs.androidx.testext.junit)
    androidTestImplementation(libs.androidx.testext.junitktx)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.runner)
}
