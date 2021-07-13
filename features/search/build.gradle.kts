import plugins.enableScreenshotTests

plugins {
    id(BuildPlugins.Ids.androidModule)
}

enableScreenshotTests()

dependencies {
    implementation(project(":platform:networking"))
    implementation(project(":platform:rest-chucknorris"))
    implementation(project(":platform:persistance"))
    implementation(project(":platform:logger"))
    implementation(project(":platform:navigator"))
    implementation(project(":platform:shared-assets"))
    implementation(project(":platform:shared-utilities"))

    implementation("org.kodein.di:kodein-di-jvm:7.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    testImplementation(project(":platform:testing-app"))
    testImplementation(project(":platform:testing-commons"))
    testImplementation(project(":platform:testing-persistance"))
    testImplementation(project(":platform:testing-rest"))

    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.robolectric:robolectric:4.6.1")
    testImplementation("androidx.test:core:1.4.0")
    testImplementation("androidx.test:core-ktx:1.4.0")
    testImplementation("androidx.test.ext:junit:1.1.3")
    testImplementation("androidx.test.ext:junit-ktx:1.1.3")
    testImplementation("androidx.test:runner:1.4.0")
    testImplementation("androidx.test:rules:1.4.0")

    androidTestImplementation(project(":platform:testing-screenshots"))

    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:core:1.4.0")
    androidTestImplementation("androidx.test:core-ktx:1.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.3")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("com.karumi:shot-android:5.11.0")
}
