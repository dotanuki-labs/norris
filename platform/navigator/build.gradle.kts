
plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {

    implementation(project(":platform:shared-utilities"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.32")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.activity:activity:1.3.0-alpha04")
    implementation("androidx.activity:activity-ktx:1.2.0-beta02")
    implementation("org.kodein.di:kodein-di-jvm:7.3.1")

    testImplementation(project(":platform:coroutines-testutils"))
    testImplementation("org.robolectric:robolectric:4.5.1")
    testImplementation("junit:junit:4.13")
    testImplementation("org.assertj:assertj-core:3.16.1")
}
