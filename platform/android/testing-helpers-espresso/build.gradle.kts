
plugins {
    id("io.dotanuki.gradle.automodule")
}

dependencies {
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.espresso.intents)
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.test.corektx)
    implementation(libs.square.radiography)
}
