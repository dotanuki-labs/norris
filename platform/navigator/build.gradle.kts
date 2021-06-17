
plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {

    implementation(project(":platform:shared-utilities"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.10")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.activity:activity:1.3.0-beta02")
    implementation("androidx.activity:activity-ktx:1.3.0-beta01")
    implementation("org.kodein.di:kodein-di-jvm:7.6.0")

    testImplementation(project(":platform:testing-commons"))
    testImplementation("org.robolectric:robolectric:4.5.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
}
