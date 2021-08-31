plugins {
    id("norris.modules.android.feature")
}

dependencies {

    implementation(projects.platform.common.commonAndroid)
    implementation(projects.platform.common.commonKodein)
    implementation(projects.platform.common.commonStatic)
    implementation(projects.platform.core.coreNavigator)
    implementation(projects.platform.core.coreNetworking)
    implementation(projects.platform.core.coreRest)
    implementation(projects.platform.core.corePersistance)

    implementation("org.kodein.di:kodein-di-jvm:7.7.0")
    implementation("org.kodein.type:kodein-type-jvm:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.3.1")
    implementation("androidx.lifecycle:lifecycle-common:2.3.1")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.1.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.core:core:1.6.0")
    implementation("com.google.android.material:material:1.4.0")

    testImplementation(projects.platform.testing.testingApp)
    testImplementation(projects.platform.testing.testingCommons)
    testImplementation(projects.platform.testing.testingRest)
    testImplementation(projects.platform.testing.testingPersistance)

    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.4.0")
    testImplementation("androidx.test.ext:junit:1.1.3")

    androidTestImplementation(projects.platform.testing.testingScreenshots)

    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:core:1.4.0")
    androidTestImplementation("androidx.test:core-ktx:1.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.3")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("com.karumi:shot-android:5.11.2")
}
