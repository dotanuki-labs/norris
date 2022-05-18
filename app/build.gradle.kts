import conventions.isTestMode

plugins {
    id("norris.modules.android.app")
}

dependencies {
    implementation(projects.platform.core.coreRest)
    implementation(projects.platform.core.corePersistance)
    implementation(projects.platform.core.coreNavigator)
    implementation(projects.platform.common.commonAndroid)
    implementation(projects.platform.common.commonKodein)
    implementation(projects.platform.common.commonStatic)
    implementation(projects.features.facts)
    implementation(projects.features.search)

    implementation(libs.kodein.di.jvm)
    implementation(libs.kodein.type.jvm)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.jvm)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.common)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.okhttp.core)

    if (isTestMode()) {
        releaseImplementation(libs.leak.canary.release)
    }

    androidTestImplementation(projects.platform.testing.testingPersistance)
    androidTestImplementation(projects.platform.testing.testingRest)

    androidTestImplementation(libs.junit4)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.corektx)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.testext.junit)
    androidTestImplementation(libs.androidx.testext.junitktx)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.okhttp.mockwebserver)
    androidTestImplementation(libs.radiography)

    androidTestImplementation(libs.barista) {
        exclude(group = "org.jetbrains.kotlin")
        exclude(group = "org.checkerframework")
    }
}
