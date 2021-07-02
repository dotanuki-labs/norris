
plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {

    implementation(project(":platform:logger"))
    implementation(project(":platform:rest-chucknorris"))
    implementation(project(":platform:networking"))
    implementation(project(":platform:persistance"))
    implementation(project(":platform:navigator"))
    implementation(project(":platform:shared-assets"))
    implementation(project(":platform:shared-utilities"))
    implementation(project(":platform:testing-rest"))

    implementation("org.kodein.di:kodein-di-jvm:7.6.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("org.robolectric:robolectric:4.5.1")
    implementation("junit:junit:4.13.2")
    implementation("androidx.test:core:1.4.0")
    implementation("androidx.test:core-ktx:1.4.0")
    implementation("androidx.test.ext:junit:1.1.3")
    implementation("androidx.test.ext:junit-ktx:1.1.2")
    implementation("androidx.test:runner:1.4.0")
}
