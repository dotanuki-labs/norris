
plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {

    implementation(project(":platform:logger"))
    implementation(project(":platform:shared-assets"))
    implementation(project(":platform:shared-utilities"))
    implementation(project(":platform:navigator"))
    implementation(project(":platform:networking"))
    implementation(project(":platform:rest-chucknorris"))
    implementation(project(":platform:persistance"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.20")
    implementation("org.kodein.di:kodein-di-jvm:7.6.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.activity:activity:1.3.0-beta02")
    implementation("androidx.activity:activity-ktx:1.3.0-beta02")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    testImplementation(project(":platform:testing-app"))

    testImplementation(project(":platform:testing-commons"))
    testImplementation(project(":platform:testing-rest"))

    testImplementation("app.cash.turbine:turbine:0.5.2")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("com.github.ubiratansoares:burster:0.1.1")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.robolectric:robolectric:4.5.1")
    testImplementation("androidx.test:core:1.4.0")
    testImplementation("androidx.test:core-ktx:1.4.0")
    testImplementation("androidx.test.ext:junit:1.1.2")
    testImplementation("androidx.test.ext:junit-ktx:1.1.2")
    testImplementation("androidx.test:runner:1.3.0")
    testImplementation("androidx.test:rules:1.3.0")
}
