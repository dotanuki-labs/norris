
plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {

    implementation(project(":platform:shared-utilities"))

    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("org.kodein.di:kodein-di-jvm:7.7.0")
    implementation("org.kodein.type:kodein-type-jvm:1.7.1")

    testImplementation(project(":platform:testing-commons"))
    testImplementation("org.robolectric:robolectric:4.6.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
}
