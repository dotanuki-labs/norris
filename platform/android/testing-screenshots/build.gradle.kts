
plugins {
    id("io.dotanuki.gradle.automodule")
}

dependencies {
    implementation(libs.junit4)
    implementation(libs.dropshots)
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.test.corektx)
    implementation(libs.androidx.test.rules)
    implementation(libs.androidx.testext.junit)
}
