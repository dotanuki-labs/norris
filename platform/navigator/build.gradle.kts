
plugins {
    id(BuildPlugins.Ids.androidModule)
}

dependencies {

    implementation(project(":platform:shared-utilities"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.10")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.activity:activity:1.2.0-beta02")
    implementation("androidx.activity:activity-ktx:1.3.0-beta01")
    implementation("org.kodein.di:kodein-di-jvm:7.6.0")

    testImplementation(project(":platform:coroutines-testutils"))
    testImplementation("org.robolectric:robolectric:4.5.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.assertj:assertj-core:3.16.1")
}
