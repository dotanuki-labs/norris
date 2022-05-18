
plugins {
    id("norris.modules.android.platform")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.androidx.test.core)
    implementation(libs.androidx.test.corektx)
    implementation(libs.androidx.lifecycle.common)
}
