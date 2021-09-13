
plugins {
    id("norris.modules.android.platform")
}

dependencies {
    implementation(projects.platform.common.commonKodein)

    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("org.kodein.di:kodein-di-jvm:7.8.0")
    implementation("org.kodein.type:kodein-type-jvm:1.8.0")

    testImplementation(projects.platform.testing.testingCommons)
    testImplementation("org.robolectric:robolectric:4.6.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")
}
