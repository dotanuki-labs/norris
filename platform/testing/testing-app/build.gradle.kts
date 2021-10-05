
plugins {
    id("norris.modules.android.platform")
}

dependencies {

    implementation(projects.platform.core.coreNavigator)
    implementation(projects.platform.core.coreRest)
    implementation(projects.platform.core.corePersistance)

    implementation("androidx.lifecycle:lifecycle-common:2.3.1")
    implementation("org.kodein.di:kodein-di-jvm:7.8.0")
    implementation("org.kodein.type:kodein-type-jvm:1.8.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.2")
    implementation("org.robolectric:robolectric:4.6.1")
    implementation("androidx.test:core:1.4.0")
    implementation("androidx.test:core-ktx:1.4.0")
    implementation("androidx.test:monitor:1.4.0")
}
