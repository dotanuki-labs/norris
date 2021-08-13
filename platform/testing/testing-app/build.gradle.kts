
plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {

    implementation(project(":platform:core:rest-chucknorris"))
    implementation(project(":platform:core:persistance"))
    implementation(project(":platform:core:navigator"))

    implementation("androidx.lifecycle:lifecycle-common:2.3.1")
    implementation("org.kodein.di:kodein-di-jvm:7.7.0")
    implementation("org.kodein.type:kodein-type-jvm:1.7.1")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("org.robolectric:robolectric:4.6.1")
    implementation("androidx.test:core:1.4.0")
    implementation("androidx.test:core-ktx:1.4.0")
    implementation("androidx.test:monitor:1.4.0")
}
